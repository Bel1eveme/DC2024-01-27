package org.education.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "distcomp";
    }

    @Bean
    public CassandraTemplate cassandraTemplate(CqlSession session, MappingCassandraConverter converter) {
        return new CassandraTemplate(session, converter);
    }

    @Override
    public CqlSessionFactoryBean cassandraSession() {
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setKeyspaceName(getKeyspaceName());
        session.setContactPoints("localhost");
        session.setPort(9042);
        session.setLocalDatacenter("datacenter1");
        return session;
    }
}
