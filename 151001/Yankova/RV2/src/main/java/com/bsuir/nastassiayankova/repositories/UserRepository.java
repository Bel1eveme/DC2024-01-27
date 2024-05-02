package com.bsuir.nastassiayankova.repositories;

import com.bsuir.nastassiayankova.beans.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
