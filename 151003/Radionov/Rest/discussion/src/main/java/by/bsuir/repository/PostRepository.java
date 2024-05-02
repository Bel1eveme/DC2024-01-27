package by.bsuir.repository;

import by.bsuir.entities.Post;
import by.bsuir.entities.PostKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CassandraRepository<Post, PostKey> {
    List<Post> findByTweetId(Long tweetId);
    List<Post> findById (Long id);
    void deleteByCountryAndTweetIdAndId (String country, Long tweetId, Long id);
    int countByCountry (String country);
}
