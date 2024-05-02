package com.example.discussion.repository;

import com.example.discussion.model.Note;
import com.example.discussion.model.NoteKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NoteRepository extends CassandraRepository<Note, NoteKey> {
    List<Note> findAll();
    void deleteByCountryAndIssueIdAndId(String country, int issueId, int id);
    List<Note> findById (int id);

    Double countByCountry(String country);
}
