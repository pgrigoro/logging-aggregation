package org.thrift.logging.aggregation.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thrift.logging.aggregation.service.LogEventGenerator;

@Component
public class EventLogScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogScheduler.class);
    
    private final LogEventGenerator generator;

    public EventLogScheduler(LogEventGenerator generator) {
        this.generator = generator;
    }
    
    @Scheduled(fixedRateString = "${scheduling.logevent.fixedrate}")
    public void scheduleLogEvents() {
        LOGGER.info("Starting logging event generation ...");
        generator.generate();
    }
  
}