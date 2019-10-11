package org.thrift.logging.aggregation.repository;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;
import org.thrift.logging.aggregation.domain.LogEvent;

@Repository
public class LogEventRepository {

    private final Mapper<LogEvent> mapper;

    public LogEventRepository(Session session) {
        this.mapper = new MappingManager(session).mapper(LogEvent.class);
    }
    
    public LogEvent save(LogEvent event) {
        mapper.save(event);
        return event;
    }
}
