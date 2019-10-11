package org.thrift.logging.aggregation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.LogEvent;
import org.thrift.logging.aggregation.LogEventService;
import org.thrift.logging.aggregation.dto.LogEventDTO;
import org.thrift.logging.aggregation.service.ProducerService;

@Service
public class LogEventServiceImpl implements LogEventService.Iface {

    private final ProducerService producerService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public LogEventServiceImpl(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void publish(LogEvent event) {
        LogEventDTO eventDto = objectMapper.convertValue(event, LogEventDTO.class);
        producerService.publishEvent(eventDto);
    }
}
