package by.bsuir.discussionservice.repository;

import by.bsuir.discussionservice.entity.Message;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CassandraRepository<Message, Message.Key> {
    @AllowFiltering
    Optional<Message> findByKey_Id(Long id);

    @AllowFiltering
    Slice<Message> findAllByKey_StoryId(Long storyId, Pageable pageable);

    @AllowFiltering
    boolean existsByKey_Id(Long id);
}
