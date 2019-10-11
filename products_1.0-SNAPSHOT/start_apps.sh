#!/bin/bash
java -jar thrift-server-1.0-SNAPSHOT.jar &
java -jar thrift-client-1.0-SNAPSHOT.jar & 
java -jar kafka-consumer-1.0-SNAPSHOT.jar