package com.example.publicator.repository;

import com.example.publicator.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag,Integer> {
    Page<Tag> findAll(Pageable pageable);
}
