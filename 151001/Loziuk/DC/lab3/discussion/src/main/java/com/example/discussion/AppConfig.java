package com.example.discussion;

import com.datastax.oss.driver.api.core.CqlSession;
import com.example.discussion.repository.NoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackageClasses = {NoteRepository.class})

public class AppConfig {

    /*
     * Use the standard Cassandra driver API to create a com.datastax.oss.driver.api.core.CqlSession instance.
     */
    public @Bean CqlSession session() {
        return CqlSession.builder().withKeyspace("distcomp").build();
    }
}