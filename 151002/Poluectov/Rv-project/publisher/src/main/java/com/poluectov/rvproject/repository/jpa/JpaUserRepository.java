package com.poluectov.rvproject.repository.jpa;

import com.poluectov.rvproject.model.User;
import com.poluectov.rvproject.repository.ICommonRepository;
import com.poluectov.rvproject.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> { }
