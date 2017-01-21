# kafka &middot; spark streaming example

This is a simple dashboard example on Kafka and Spark Streaming

### Prerequisites

Java 1.8 or newer version required because lambda expression used for few cases

1. Java >= 1.8 (Oracle JDK has been tested)
2. Maven >= 3
3. Apache Spark >= 2.0.2
4. Kafka >= 0.10.1.0

### Installation

First of all, clone the git repository,

```bash
$ git clone git@github.com:trK54Ylmz/kafka-spark-streaming-example.git
```

after you need to use Maven for creating uber jar files,

```bash
$ mvn clean package -DskipTests
```

### Usage

1 - Start the Spark streaming service and it'll process events from Kafka topic to MySQL,

```bash
$ cd kafka-spark-streaming-example
$ java -Dconfig=./config/common.conf -jar streaming/target/spark-streaming-0.1.jar
```

2 - Start the Kafka producer and it'll write events to Kafka topic,

```bash
$ java -Dconfig=./config/common.conf -jar producer/target/kafka-producer-0.1.jar
```

3 - Start the web server so you can see the dashboard

```bash
$ java -Dconfig=./config/common.conf -jar producer/target/web-0.1.jar
```

4 - If everything look fine, please enter the dashboard address,

```bash
open http://localhost:8080 # default value : 8080
```

![screen](screen.png)