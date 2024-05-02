package com.poluectov.rvproject.repository.customjpa;

import com.poluectov.rvproject.model.Issue;
import com.poluectov.rvproject.repository.IssueRepository;
import com.poluectov.rvproject.repository.jpa.JpaIssueRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomJpaIssueRepository extends CustomJpaRepository<Issue, Long> implements IssueRepository {


    @Autowired
    public CustomJpaIssueRepository(
            JpaIssueRepository jpaMarkerRepository,
            EntityManager entityManager) {
        super(jpaMarkerRepository, entityManager, "Issue");
    }
}
