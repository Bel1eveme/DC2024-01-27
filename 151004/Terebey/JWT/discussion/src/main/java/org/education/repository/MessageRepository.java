package org.education.repository;

import org.education.bean.Message;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    @AllowFiltering
    Optional<Message> findMessageById(int id);
    @AllowFiltering
    boolean existsById(int id);
}