package org.thrift.logging.aggregation;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.thrift.logging.aggregation.cfg.ServerConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final ServerConfig serverConfig;
    private final LogEventService.Iface logEventService;
            
    private TServer tServer;

    public Server(ServerConfig serverConfig, LogEventService.Iface logEventService) {
        this.serverConfig = serverConfig;
        this.logEventService = logEventService;
    }

    @PostConstruct
    public void doStart() {
        try {
            TServerTransport serverTransport = new TServerSocket(serverConfig.getPort());
            tServer = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport)
                    .processor(new LogEventService.Processor(logEventService)));
            LOGGER.info("Server listening on port: {}", serverConfig.getPort());
            tServer.serve();
        } catch (TTransportException e) {
            throw new RemoteAccessException("Server cannot open socket", e);
        }
    }

    @PreDestroy
    public void doShutdown() {
        if (tServer != null) {
            tServer.stop();
        }
        tServer = null;
    }
}