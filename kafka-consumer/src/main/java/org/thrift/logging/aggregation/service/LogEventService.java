package org.thrift.logging.aggregation.service;

import org.thrift.logging.aggregation.dto.LogEventDTO;

public interface LogEventService {

    LogEventDTO save(LogEventDTO logEventDTO);
    
}
