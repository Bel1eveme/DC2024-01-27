package by.rusakovich.discussion.spi.dao;

import by.rusakovich.discussion.model.Note;
import by.rusakovich.discussion.model.NoteKey;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends CassandraRepository<Note, NoteKey> {
    @AllowFiltering
    List<Note> findById(Long id);
    void deleteByCountryAndNewsIdAndId(String country, Long newsId, Long id);
}
