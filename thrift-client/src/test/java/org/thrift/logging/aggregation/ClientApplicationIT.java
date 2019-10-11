package org.thrift.logging.aggregation;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thrift.logging.aggregation.cfg.ClientConfig;
import org.thrift.logging.aggregation.service.LogEventGenerator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
public class ClientApplicationIT {

    @Autowired
    private ClientConfig clientProperties;
    
    @Autowired
    private LogEventGenerator generator;

    private MockEventReceiver mockEventReceiver;
    private TServer server;
    private Thread serverThread;
            
    @Before
    public void before() {
        mockEventReceiver = new MockEventReceiver();
        LogEventService.Processor processor = new LogEventService.Processor<>(mockEventReceiver);
        
        serverThread = new Thread(() -> {
            try {
                TServerTransport serverTransport = new TServerSocket(clientProperties.getServerInfo().getServerPort());
                server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
                server.serve();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
    }

    @After
    public void after() {
        if (server != null) {
            server.stop();
        }
        server = null;
    }    

    @Test
    public void generateLogEventTest() {
        assertNull(mockEventReceiver.getEvent());
        generator.generate();
        assertNotNull(mockEventReceiver.getEvent());
    }

    private static class MockEventReceiver implements LogEventService.Iface {
        private LogEvent event;
                
        @Override
        public void publish(LogEvent event) {
            this.event = event;
        }

        public LogEvent getEvent() {
            return event;
        }        
    }    
}
