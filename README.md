Logging Aggregation
===================


This is an aggregation/ingestion system for logging events. 
The system composed by 3 sub-systems:
  * A client `(thrift-client)` that sends randomly generated logging events to a server.
  * A Producer Server `(thrift-server)` that accepts the events and pushes them to Kafka.
  * A Consumer Server `(kafka-consumer)` that consumes the log events and writes them to a Cassandra database.


Supporting modules
----------------
  * `thrift-common`: Contains the thrift API (defined in `logging.thrift` file)
  * `types`: Contains dtos and utilities


Build
----------------
* In order to build the applications, run the `build-apps.sh` script. The result of this script is to populated the `binary` directory with the required jars.


Execution
----------------
1. Ensure that the environment is up and running (Zookeeper, Kafka & Cassandra).
The `Apache Cassandra` is running at port `9042`.
The `Apache Kafka` bootstrap server is running at port `9092`.
The topic log-events is already created (bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic log-events).

2. The script `binary/start-consumer.sh` starts the consumer module.

3. The script `binary/start-server.sh` starts the server (producer) module. The server is listening at port `9098`.

4. The script `binary/start-client.sh` starts the client module.


Implementation Details
----------------------
* The Client is scheduled (using Spring Scheduling) to create and send a log event every second.  
* The Server (acting as kafka message producer) receives the log events from the client and sends them to `Kafka`.
* The Consumer (kafka message Consumer) consumes log events from `Kafka` and persists them to `Cassandra`. The Consumer, during startup checks if there is a need to create the keyspace logging and table log_event.


Database Design
----------------------
Table log_event:

|Column		   |Type      |Description								  |
|------------------|----------|---------------------------------------------------------------------------|
|date_id           |INT	      |First member of the partitioned key that represents a date (e.g. 20191011) |
|log_level         |TEXT      |The log level (e.g. TRACE, DEBUG, INFO, WARN, ERROR, FATAL)                |
|host_ip           |TEXT      |The IP address in the host where the event was generated.                  |
|app_name          |TEXT      |The client application name where the event was generated.                 |
|uuid              |TEXT      |The event uuid.							          |
|user_name         |TEXT      |The the user related to the generated event.			          |
|creation_date_utc |TIMESTAMP |The UTC time of the event (e.g. 2019-10-11 13:50:24.054000+0000)           |
|message           |TEXT      |The event message.							  |
|version           |INT       |The event version.							  |

Primary key: date_id, app_name, log_level, creation_date_utc, uuid
Partition key: date_id, app_name, log_level
clustering key: creation_date_utc DESC, uuid DESC

The above configuration selected in order to improve the following queries:
Query 1 (For a specific day check an application for errors):
SELECT * FROM log_event where date_id=20191011 and app_name='OpenJDK 64-Bit S - 1237' and log_level = 'ERROR';

Query 2 (For some days check an application for fatal):
SELECT * FROM log_event where date_id in (20191009, 20191011, 20191012) and app_name='OpenJDK 64-Bit S - 1237' and log_level = 'FATAL';

Query 3 (For a specific day check an application for errors between 3 minutes):
SELECT * FROM log_event where date_id=20191011 and app_name='OpenJDK 64-Bit S - 1237' and log_level = 'ERROR'
	and creation_date_utc >= ' 2019-10-11 11:49:44' and creation_date_utc <= ' 2019-10-11 11:52:44';

Query 4 (For a specific day check some applications for errors or warnings):
SELECT * FROM log_event where date_id=20191011 and app_name in ('OpenJDK 64-Bit S - 1237', 'OpenJDK 64-Bit S - 1239')
	and log_level in ('ERROR', 'WARN'); 
 
Query 5 (Last 10 errors for a specific day and application):
SELECT * FROM log_event where date_id=20191011 and app_name in ('OpenJDK 64-Bit S - 1232')  and log_level in ('ERROR') limit 10;

The following are the results of the 5th query and I think this it is very important to support this kind of queries in order to allow 
the monitoring live monitoring of applications for errors:

 date_id  | app_name                | log_level | creation_date_utc               | uuid                                 | host_ip    | message                                         | user_name | version
----------+-------------------------+-----------+---------------------------------+--------------------------------------+------------+-------------------------------------------------+-----------+---------
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:50:36.053000+0000 | 4da8b773-e647-452f-8d87-ea3c6a95f964 | 127.0.1.18 |     mobGdPDOSsaCICYq2ESYOSRjYrjL6VYqIrDVe1j4Dil |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:49:11.053000+0000 | 2213cf23-d787-4874-aef5-edee656ba398 | 127.0.1.18 |     RfQ8cHN7fSuwHSeCmUYSk5pbhvQbK2zgkcbTnyAwSTj |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:32:08.686000+0000 | b2028510-4285-4fc5-863d-d8a2ffe1606d | 127.0.1.11 |                   JN7UT7pdWZWp2X440dKarIWP0pFHz |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:31:53.689000+0000 | 9164fa2a-c97f-4228-a12f-ceec423d818f | 127.0.1.18 | yVPHjLq4SPiwtxzd5IKWpL40vkELMPwmO2VZo7IP1RrP9D9 |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:31:33.687000+0000 | 903afc8a-3eba-47ad-bd96-5fc9ec293670 | 127.0.1.15 |                            KpoxP1OvxZgqPc2YTGoJ |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 13:31:09.687000+0000 | 168859dd-dd1c-447e-a3e4-c33d21f0813c | 127.0.1.16 |                          Ugues3bjpYvsm3A5rhwBFb |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 12:59:28.354000+0000 | 67a08fbf-c5b4-4cd6-a5f5-73df4d4ff91b | 127.0.1.18 |                          7rjXrEHItLJt54xbCKZs1w |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 12:58:56.354000+0000 | 4c904be0-838b-4fc9-a7e1-78361fac3721 | 127.0.1.11 |                      UMdp2NkHTYsRHh6xPl9aB7t6wo |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 12:58:06.355000+0000 | acad1d70-0f03-4fb1-8b02-0d54f105aa93 | 127.0.1.17 |                          5S2kk0ynrcqIxa4S1pYOuv |  pgrigoro |       1
 20191011 | OpenJDK 64-Bit S - 1232 |     ERROR | 2019-10-11 12:57:35.355000+0000 | 434e6383-81c5-4ce6-80a2-48a92ddfc58f | 127.0.1.14 |                         VFXhLRzKRWGjn8Ht82ZOaok |  pgrigoro |       1

Software Versions
----------------------
* `JDK 1.8 (1.8.0_222)`
* `Spring Boot 2.1.9`
* `Apache Maven 3.6.1`
* `Apache Thrift 0.12.0`
* `Apache Kafka 2.12-2.2.1`
* `Apache Cassandra 3.6`
* `Apache Zookeeper 3.4.13`
