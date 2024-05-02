package by.poit.dtalalaev.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = {"by.poit.dtalalaev.entity"})
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "distcomp"; // замените на имя существующего ключевого пространства
    }


    @Override
    protected String getContactPoints() {
        return "localhost";
    }

    @Override
    protected int getPort() {
        return 9042;
    }

    @Bean
    @Primary
    public CqlSession session() {
        return CqlSession.builder().withKeyspace("distcomp").build();
    }

    @Bean
    public CqlSession customCassandraSession() {
        return CqlSession.builder().withKeyspace("distcomp").build();
    }

}
