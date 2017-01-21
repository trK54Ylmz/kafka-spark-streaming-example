/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test

import java.io.IOException
import java.sql.Timestamp
import java.util.Properties

import com.test.beans.RecordBean
import com.test.config.ConfigurationFactory
import com.test.utils.JsonUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext, SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferBrokers
import org.apache.spark.streaming.{Seconds, StreamingContext}

object App {
  private[this] lazy val logger = Logger.getLogger(getClass)

  private[this] val config = ConfigurationFactory.load()

  /**
    * Json decode UDF function
    *
    * @param text the encoded JSON string
    * @return Returns record bean
    */
  def jsonDecode(text: String): RecordBean = {
    try {
      JsonUtils.deserialize(text, classOf[RecordBean])
    } catch {
      case e: IOException =>
        logger.error(e.getMessage, e)
        null
    }
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("spark-kafka-streaming-example")
      .master("local[*]")
      .getOrCreate

    val streaming = new StreamingContext(spark.sparkContext, Seconds(config.getStreaming.getWindow))

    val servers = config.getProducer.getHosts.toArray.mkString(",")

    val params = Map[String, Object](
      "bootstrap.servers" -> servers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "auto.offset.reset" -> "latest",
      "group.id" -> "dashboard",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    // topic names which will be read
    val topics = Array(config.getProducer.getTopic)

    // create kafka direct stream object
    val stream = KafkaUtils.createDirectStream[String, String](
      streaming, PreferBrokers, Subscribe[String, String](topics, params))

    // our table has 3 fields called market (varchar), rate (float) and dt (datetime etc.)
    val schema = StructType(
      StructField("market", StringType) ::
        StructField("rate", FloatType) ::
        StructField("dt", TimestampType) :: Nil
    )

    val host = config.getStreaming.getDb.getHost
    val db = config.getStreaming.getDb.getDb
    val url = s"jdbc:mysql://$host/$db"
    val table = config.getStreaming.getDb.getTable

    val props = new Properties
    props.setProperty("driver", "com.mysql.jdbc.Driver")
    props.setProperty("user", config.getStreaming.getDb.getUser)
    props.setProperty("password", config.getStreaming.getDb.getPass)

    // just alias for simplicity
    type Record = ConsumerRecord[String, String]

    stream.foreachRDD((rdd: RDD[Record]) => {
      // convert string to PoJo and generate rows as tuple group
      val pairs = rdd
        .map(row => (row.timestamp(), jsonDecode(row.value())))
        .map(row => (row._2.getType.name(), (1, row._2.getValue, row._1)))

      /**
        * aggregate data by market type
        *
        * tuple has 3 items,
        * the first one is counter value and this value is 1,
        * second one is the rate and received from Kafka,
        * third one is event time. for instance `2017-05-12 16:00:00`
        *
        * in the map,
        * method <code>f._1</code> is market name,
        * we divide total rate to total item count <code>f._2._2 / f._2._1</code>
        * as you can see <code>f._2._3</code> is average event time
        **/
      val flatten = pairs
        .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2, (y._3 + x._3) / 2))
        .map(f => Row.fromSeq(Seq(f._1, f._2._2 / f._2._1, new Timestamp(f._2._3))))

      // create sql context from active spark context
      val sql = new SQLContext(flatten.sparkContext)

      // write aggregated results to database
      // only one partition required for better visualisation,
      // better to look at https://goo.gl/iBdNDl
      sql.createDataFrame(flatten, schema)
        .repartition(1)
        .write
        .mode(SaveMode.Append)
        .jdbc(url, table, props)
    })

    // create streaming context and submit streaming jobs
    streaming.start()

    // wait to killing signals etc.
    streaming.awaitTermination()
  }
}