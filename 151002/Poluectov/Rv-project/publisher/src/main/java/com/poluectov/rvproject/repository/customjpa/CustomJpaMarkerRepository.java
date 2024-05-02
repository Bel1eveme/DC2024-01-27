package com.poluectov.rvproject.repository.customjpa;

import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.repository.MarkerRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.repository.jpa.JpaMarkerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomJpaMarkerRepository extends CustomJpaRepository<Marker, Long> implements MarkerRepository {


    @Autowired
    public CustomJpaMarkerRepository(JpaMarkerRepository jpaMarkerRepository,
                                     EntityManager entityManager) {
        super(jpaMarkerRepository, entityManager, "Marker");
    }

    public List<Marker> findByIdIn(List<Long> ids) {
        return ((JpaMarkerRepository)jpaMarkerRepository).findByIdIn(ids);
    }
}
