package org.example.publisher.impl.editor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface EditorRepository extends JpaRepository<Editor, BigInteger> {

}
