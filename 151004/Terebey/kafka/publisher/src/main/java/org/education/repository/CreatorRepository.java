package org.education.repository;

import org.education.bean.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Integer> {

    boolean existsCreatorByLogin(String login);
}