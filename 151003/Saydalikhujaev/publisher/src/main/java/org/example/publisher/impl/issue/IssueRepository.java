package org.example.publisher.impl.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface IssueRepository extends JpaRepository<Issue, BigInteger> {
}