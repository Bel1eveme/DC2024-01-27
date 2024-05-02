package com.example.publicator.repository;

import com.example.publicator.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IssueRepository extends JpaRepository<Issue,Integer> {

    Page<Issue> findAll (Pageable pageable);
    boolean existsByTitle(String title);

}


