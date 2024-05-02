package com.poluectov.rvproject.repository.customjpa;

import com.poluectov.rvproject.model.User;
import com.poluectov.rvproject.repository.UserRepository;
import com.poluectov.rvproject.repository.jpa.JpaUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomJpaUserRepository extends CustomJpaRepository<User, Long> implements UserRepository {

    @Autowired
    public CustomJpaUserRepository(JpaUserRepository jpaMarkerRepository,
                                   EntityManager entityManager) {
        super(jpaMarkerRepository, entityManager, "User");
    }
}
