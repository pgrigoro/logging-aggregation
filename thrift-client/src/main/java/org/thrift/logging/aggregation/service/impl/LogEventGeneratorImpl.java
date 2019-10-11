package org.thrift.logging.aggregation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.LogEvent;
import org.thrift.logging.aggregation.LogLevel;
import org.thrift.logging.aggregation.service.LogEventGenerator;
import org.thrift.logging.aggregation.service.LogEventTransmitter;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.thrift.logging.aggregation.util.DateUtil.FULL_DATE_FORMAT;
import static org.thrift.logging.aggregation.util.DateUtil.formatUTC;

@Service
public class LogEventGeneratorImpl implements LogEventGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventGeneratorImpl.class);

    private final LogEventTransmitter eventTransmitter;
    
    public LogEventGeneratorImpl(LogEventTransmitter eventTransmitter) {
        this.eventTransmitter = eventTransmitter;
    }

    /**
     * Generates and transmits events to server
     */
    @Override
    public void generate() {
        try {
            LogEvent logEvent = createEvent();
            eventTransmitter.transmit(logEvent);
            LOGGER.info("Event transmission completed: {}", logEvent);
        } catch (RemoteAccessException e) {
            LOGGER.error("Event transmission failed", e);
        }
    }

    /**
     * Creates a new LogEvent object.
     *
     * @return the create {@link LogEvent} object
     *
     * @throws Exception
     */
    private LogEvent createEvent() {
        short v = 1;
        String uuid = UUID.randomUUID().toString();
        String time = formatUTC(new Date(), FULL_DATE_FORMAT);
        String m = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(20, 50));
        LogLevel logLevel = LogLevel.values()[new Random().nextInt(LogLevel.values().length)];
        String appName = ManagementFactory.getRuntimeMXBean().getVmName().substring(0, 16) + " - " + RandomUtils.nextInt(1230, 1239);
        String hostIp;
        try {
            hostIp = InetAddress.getLocalHost().getHostAddress() + RandomUtils.nextInt(0, 10);
        } catch (UnknownHostException e) {
            hostIp = "Unknown Host";
        }
        String userName = Optional.ofNullable(System.getProperty("user.name")).orElse("pgrigoro");
                
        LogEvent event = new LogEvent();
        event.setUuid(uuid);
        event.setV(v);
        event.setTime(time);
        event.setM(m);
        event.setLogLevel(logLevel);
        event.setHostIp(hostIp);
        event.setAppName(appName);
        event.setUserName(userName);
        return event;
    }

}