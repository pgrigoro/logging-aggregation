package org.thrift.logging.aggregation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.domain.LogEvent;
import org.thrift.logging.aggregation.dto.LogEventDTO;
import org.thrift.logging.aggregation.repository.LogEventRepository;
import org.thrift.logging.aggregation.service.LogEventService;

@Service
public class LogEventServiceImpl implements LogEventService {
    
    private final LogEventRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LogEventServiceImpl(LogEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogEventDTO save(LogEventDTO logEventDTO) {
        LogEvent event = convertToEntity(logEventDTO);
        return convertToDto(repository.save(event));
    }

    private LogEvent convertToEntity(LogEventDTO dto) {
        LogEvent entity = objectMapper.convertValue(dto, LogEvent.class);
        entity.setVersion(dto.getV());
        entity.setMessage(dto.getM());
        entity.setCreationDateUtc(dto.getTimeAsDate());
        entity.setDateId(dto.getShortTime());
        return entity;
    }

    private LogEventDTO convertToDto(LogEvent entity) {
        LogEventDTO dto = objectMapper.convertValue(entity, LogEventDTO.class);
        dto.setTimeFromDate(entity.getCreationDateUtc());
        dto.setV(entity.getVersion());
        dto.setM(entity.getMessage());
        return dto;
    }    
}
