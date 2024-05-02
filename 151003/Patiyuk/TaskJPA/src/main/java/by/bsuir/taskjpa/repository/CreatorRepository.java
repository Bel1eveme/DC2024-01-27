package by.bsuir.taskrest.repository;

import by.bsuir.taskrest.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByStories_Id(Long storyId);
}
