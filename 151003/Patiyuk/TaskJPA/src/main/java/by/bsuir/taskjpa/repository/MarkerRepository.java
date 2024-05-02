package by.bsuir.taskrest.repository;

import by.bsuir.taskrest.entity.Marker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByStories_Id(Long storyId, Pageable pageable);
}
