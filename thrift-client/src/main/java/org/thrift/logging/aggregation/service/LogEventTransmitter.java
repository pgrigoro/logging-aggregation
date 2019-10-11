package org.thrift.logging.aggregation.service;

import org.thrift.logging.aggregation.LogEvent;

public interface LogEventTransmitter {

    void transmit(LogEvent event);
    
}
