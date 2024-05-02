package com.example.publicator.repository;

import com.example.publicator.model.Creator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<Creator,Integer>{

    Page<Creator> findAll (Pageable pageable);
    boolean existsByLogin(String login);
}
