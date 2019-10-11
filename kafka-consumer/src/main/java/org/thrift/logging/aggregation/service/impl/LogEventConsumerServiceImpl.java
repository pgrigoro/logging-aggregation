package org.thrift.logging.aggregation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.dto.LogEventDTO;
import org.thrift.logging.aggregation.service.LogEventConsumerService;
import org.thrift.logging.aggregation.service.LogEventService;

import java.io.IOException;
import java.util.Optional;

@Service
public class LogEventConsumerServiceImpl implements LogEventConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventConsumerServiceImpl.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LogEventService service;

    public LogEventConsumerServiceImpl(LogEventService service) {
        this.service = service;
    }

    @Override
    @KafkaListener(topics = "${app.kafka.topic}")
    public void receive(@Payload String eventText, @Headers MessageHeaders headers) {
        LOGGER.info("Consumer -> received event: {}", eventText);
        LogEventDTO eventDTO = null;
        try {
            eventDTO = objectMapper.readValue(eventText, LogEventDTO.class);
        } catch (IOException e) {
            LOGGER.error("Consumer error while reading event: {}", e.getMessage());
        }
        
        Optional.ofNullable(eventDTO).ifPresent(service::save);
        LOGGER.info("Consumer -> saved event: {}", eventText);
    }
}
