package org.example.discussion.impl.note;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface NoteRepository extends CassandraRepository<Note, BigInteger> {
    @Query("SELECT * FROM tbl_note WHERE note_country = :note_country AND note_id = :id")
    Optional<Note> findBy_id(@Param("note_country") String country, @Param("id") BigInteger id);

    @Query("DELETE FROM tbl_note WHERE note_country = :note_country AND note_id = :id")
    void deleteBy_id(@Param("note_country") String country, @Param("id") BigInteger id);
}