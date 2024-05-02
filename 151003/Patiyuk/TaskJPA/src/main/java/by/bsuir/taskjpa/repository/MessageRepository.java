package by.bsuir.taskrest.repository;

import by.bsuir.taskrest.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByStoryId(Long storyId);
}
