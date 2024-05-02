package by.bsuir.ilya.repositories;

import by.bsuir.ilya.Entity.Post;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CassandraRepository<Post, Long> {
    @Query("select * from post where id = ?0 allow filtering;")
    Optional<Post> findById(Long id);
}

