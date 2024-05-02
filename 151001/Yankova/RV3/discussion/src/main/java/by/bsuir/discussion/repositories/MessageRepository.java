package by.bsuir.discussion.repositories;

import by.bsuir.discussion.domain.Message;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends MapIdCassandraRepository<Message> {
    @AllowFiltering
    void deleteMessageByNewsIdAndId(Long id, Long uuid);
    @AllowFiltering
    Optional<Message> findMessageById(Long id);
    Optional<Message> findMessageByNewsIdAndId(Long id, Long uuid);
}