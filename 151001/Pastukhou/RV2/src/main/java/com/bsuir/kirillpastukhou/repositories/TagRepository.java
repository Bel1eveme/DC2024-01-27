package com.bsuir.kirillpastukhou.repositories;

import com.bsuir.kirillpastukhou.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
