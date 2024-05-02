package org.education.repository;

import org.education.bean.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
    boolean existsIssueByTitle(String title);
}