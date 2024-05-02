package by.bsuir.romankokarev.repository;

import by.bsuir.romankokarev.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    Page<News> findAll(Pageable pageable);

    boolean existsByTitle(String title);

}


