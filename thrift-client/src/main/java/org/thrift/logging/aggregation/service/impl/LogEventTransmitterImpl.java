package org.thrift.logging.aggregation.service.impl;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;
import org.thrift.logging.aggregation.LogEvent;
import org.thrift.logging.aggregation.LogEventService;
import org.thrift.logging.aggregation.cfg.ClientConfig;
import org.thrift.logging.aggregation.service.LogEventTransmitter;

@Service
public class LogEventTransmitterImpl implements LogEventTransmitter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventTransmitterImpl.class);

    private final ClientConfig clientConfig;

    public LogEventTransmitterImpl(ClientConfig clientProperties) {
        this.clientConfig = clientProperties;
    }
    
    @Override
    public void transmit(LogEvent event) {
        LogEventService.Client client = connect();

        try {
            client.publish(event);
        } catch (TException e) {
            throw new RemoteAccessException("Event transmission failed", e);
        } finally {
            disconnect(client);
        }

    }

    /**
     * Opens a new transport instance
     *
     * @return The created transport instance
     *
     */
    private LogEventService.Client connect() {
        LogEventService.Client client;
        TTransport transport;
        try {
            transport = new TSocket(clientConfig.getServerInfo().getServerIp(), clientConfig.getServerInfo().getServerPort());
            transport.open();
            client = new LogEventService.Client(new TBinaryProtocol(transport));

        } catch (TTransportException e) {
            throw new RemoteAccessException("Cannot open connection", e);
        }
        return client;
    }

    /**
     * Disconnects the given {@link LogEventService.Client} instance quietly
     * @param client
     */
    private void disconnect(LogEventService.Client client) {
        try {
            client.getOutputProtocol().getTransport().close();
        } catch(Exception e) {
            LOGGER.error("Cannot close output protocol transport", e);
        }

        try {
            client.getInputProtocol().getTransport().close();
        } catch(Exception e) {
            LOGGER.error("Cannot close input protocol transport", e);
        }
    }

}
