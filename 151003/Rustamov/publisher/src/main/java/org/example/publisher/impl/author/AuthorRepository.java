package org.example.publisher.impl.author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface AuthorRepository extends JpaRepository<Author, BigInteger> {

}
