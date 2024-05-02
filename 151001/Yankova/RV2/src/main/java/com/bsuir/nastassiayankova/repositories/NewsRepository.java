package com.bsuir.nastassiayankova.repositories;

import com.bsuir.nastassiayankova.beans.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
