package org.thrift.logging.aggregation.cfg;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.revinate.henicea.migration.Migrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@Configuration
public class CassandraConfig {

    @Autowired
    private ResourcePatternResolver resourceResolver;

    @Autowired
    private Environment environment;

    @Bean
    public Migrator migrator() {
        return new Migrator();
    }

    @Bean
    public Cluster cluster() throws IOException {
        Cluster cluster = Cluster.builder()
                .withoutJMXReporting()
                .addContactPoints(environment.getProperty("spring.data.cassandra.contact-points").split(","))
                .withPort(environment.getProperty("spring.data.cassandra.port", Integer.class))
                .build();

        migrator().execute(cluster, environment.getProperty("spring.data.cassandra.keyspace-name"), getMigrations());

        return cluster;
    }

    @Bean
    public Session session() throws IOException {
        return cluster().connect(environment.getProperty("spring.data.cassandra.keyspace-name"));
    }

    private Resource[] getMigrations() throws IOException {
        return resourceResolver.getResources("classpath:/cassandra/*.cql");
    }
}