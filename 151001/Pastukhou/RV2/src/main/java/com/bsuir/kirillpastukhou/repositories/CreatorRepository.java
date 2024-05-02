package com.bsuir.kirillpastukhou.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bsuir.kirillpastukhou.domain.entity.Creator;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
}
