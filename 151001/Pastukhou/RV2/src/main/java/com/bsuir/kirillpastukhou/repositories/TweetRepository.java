package com.bsuir.kirillpastukhou.repositories;

import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
