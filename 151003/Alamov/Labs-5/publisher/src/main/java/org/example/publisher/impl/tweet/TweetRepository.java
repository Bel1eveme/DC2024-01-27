package org.example.publisher.impl.tweet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface TweetRepository extends JpaRepository<Tweet, BigInteger> {
}