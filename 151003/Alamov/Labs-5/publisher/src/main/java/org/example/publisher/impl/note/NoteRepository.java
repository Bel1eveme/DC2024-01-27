package org.example.publisher.impl.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public interface NoteRepository extends JpaRepository<Note, BigInteger> {
}