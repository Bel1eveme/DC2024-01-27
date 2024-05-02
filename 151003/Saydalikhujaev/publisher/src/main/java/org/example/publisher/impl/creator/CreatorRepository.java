package org.example.publisher.impl.creator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface CreatorRepository extends JpaRepository<Creator, BigInteger> {

}
