package com.example.rv.impl.tag;

import com.example.rv.api.MemRepository.MemoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public interface TagRepository extends JpaRepository<Tag, BigInteger> {

}
