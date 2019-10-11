package org.thrift.logging.aggregation.service;

import org.springframework.messaging.MessageHeaders;

public interface LogEventConsumerService {
    
    void receive(String eventText, MessageHeaders headers);
}
