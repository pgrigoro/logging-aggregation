package org.thrift.logging.aggregation.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${thrift.server.ip}")
    private String serverIp;

    @Value("${thrift.server.port}")
    private int serverPort;

    private ServerInfo serverInfo;

    public ServerInfo getServerInfo() {
        if (serverInfo == null) {
            serverInfo = ServerInfo.builder()
                    .serverIp(serverIp)
                    .serverPort(serverPort).build();
        }

        return serverInfo;
    }

    public static class ServerInfo {
        private String serverIp;
        private int serverPort;

        public String getServerIp() {
            return serverIp;
        }

        public int getServerPort() {
            return serverPort;
        }

        private ServerInfo() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {

            private String serverIp;
            private int serverPort;

            public Builder serverIp(String serverIp) {
                this.serverIp = serverIp;
                return this;
            }

            public Builder serverPort(int serverPort) {
                this.serverPort = serverPort;
                return this;
            }

            public ServerInfo build() {
                ServerInfo server = new ServerInfo();
                server.serverPort = this.serverPort;
                server.serverIp = this.serverIp;
                return server;
            }
        }

    }

}
