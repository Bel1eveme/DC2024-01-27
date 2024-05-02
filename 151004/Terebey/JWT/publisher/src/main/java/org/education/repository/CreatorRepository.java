package org.education.repository;

import org.education.bean.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Integer> {

    boolean existsCreatorByLogin(String login);
    UserDetails findByLogin(String login);
}