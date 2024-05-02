package by.bsuir.ilya.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration

public class CassandraConfig {
    public @Bean CqlSession session(){
        return CqlSession.builder().withKeyspace("distcomp").build();
    }
}