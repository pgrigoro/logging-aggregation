package org.thrift.logging.aggregation.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.LogEvent;
import org.thrift.logging.aggregation.dto.LogEventDTO;
import org.thrift.logging.aggregation.service.ProducerService;

import java.util.Optional;

@Service
public class ProducerServiceImpl implements ProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerServiceImpl.class);

    @Value("${app.kafka.topic}")
    private String topic;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, LogEvent> kafkaTemplate;
    
    public ProducerServiceImpl(KafkaTemplate<String, LogEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishEvent(LogEventDTO dto) {
        LOGGER.info("Producer -> sending event: {}", dto);
        Message<String> message = null;
        try {
            message = MessageBuilder
                    .withPayload(objectMapper.writeValueAsString(dto))
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();
        } catch (JsonProcessingException e) {
            LOGGER.error("Producer error while serializing event: {}", e.getMessage());
        }
        
        Optional.ofNullable(message).ifPresent(kafkaTemplate::send);
        LOGGER.info("Producer -> sent event: {}", dto);
    }
}