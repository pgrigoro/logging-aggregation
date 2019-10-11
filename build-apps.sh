#!/bin/sh

mvn clean package
mv kafka-consumer/target/kafka-consumer-1.0-SNAPSHOT.jar binary/kafka-consumer-1.0-SNAPSHOT.jar
mv thrift-client/target/thrift-client-1.0-SNAPSHOT.jar binary/thrift-client-1.0-SNAPSHOT.jar
mv thrift-server/target/thrift-server-1.0-SNAPSHOT.jar binary/thrift-server-1.0-SNAPSHOT.jar