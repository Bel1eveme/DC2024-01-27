package com.poluectov.reproject.discussion.repository;

import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.model.MessageKey;
import org.hibernate.annotations.processing.HQL;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CassandraRepository<Message, MessageKey> {

    List<Message> findByIssueId(Long issueId);


    // Due to this application structure we should use allow
    // filtering by id so we can connect with publisher microservice

    @Query("SELECT * FROM distcomp.tbl_message WHERE id = :id ALLOW FILTERING")
    List<Message> findById (Long id);
    void deleteByCountryAndIssueIdAndId (String country, Long issueId, Long id);
    int countByCountry (String country);

    @Query("DELETE FROM distcomp.tbl_message WHERE country = :country AND issueid = :issueId AND id = :id")
    void deleteById(String country, Long issueId, Long id);
}
