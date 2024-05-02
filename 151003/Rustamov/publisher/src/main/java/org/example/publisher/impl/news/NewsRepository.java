package org.example.publisher.impl.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface NewsRepository extends JpaRepository<News, BigInteger> {
}