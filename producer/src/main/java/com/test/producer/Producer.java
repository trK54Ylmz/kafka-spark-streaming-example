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
package com.test.producer;

import com.test.config.ConfigurationFactory;
import com.test.config.objects.Config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    private static final Config CONFIG = ConfigurationFactory.load();

    private final KafkaProducer<String, String> producer;

    public Producer() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0, size = CONFIG.getProducer().getHosts().size(); i < size; i++) {
            builder.append(CONFIG.getProducer().getHosts().get(i));

            if (i < size - 1) {
                builder.append(",");
            }
        }

        final Properties props = new Properties();
        props.put("bootstrap.servers", builder.toString());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", CONFIG.getProducer().getBatchSize());
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void produce(String key, String data) {
        producer.send(new ProducerRecord<>(CONFIG.getProducer().getTopic(), key, data));
    }

    public void close() {
        producer.close();
    }
}