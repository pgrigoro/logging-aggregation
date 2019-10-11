package org.thrift.logging.aggregation.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Value("${thrift.server.port}")
    private int port;

    public int getPort() {
        return port;
    }
}
