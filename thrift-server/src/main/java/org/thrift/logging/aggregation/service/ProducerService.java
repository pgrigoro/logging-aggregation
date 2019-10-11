package org.thrift.logging.aggregation.service;

import org.thrift.logging.aggregation.dto.LogEventDTO;

public interface ProducerService {
    
    void publishEvent(LogEventDTO eventDto);
}
