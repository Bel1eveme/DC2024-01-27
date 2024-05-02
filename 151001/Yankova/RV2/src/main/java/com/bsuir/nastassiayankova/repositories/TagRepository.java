package com.bsuir.nastassiayankova.repositories;

import com.bsuir.nastassiayankova.beans.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
