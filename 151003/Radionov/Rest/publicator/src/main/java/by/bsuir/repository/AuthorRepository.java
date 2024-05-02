package by.bsuir.repository;

import by.bsuir.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    boolean existsByLogin(String login);

    @Query("SELECT i.author FROM Tweet i WHERE i.id = :tweetId")
    Author findAuthorByTweetId(@Param("tweetId") Long tweetId);

    Page<Author> findAll(Pageable pageable);
}
