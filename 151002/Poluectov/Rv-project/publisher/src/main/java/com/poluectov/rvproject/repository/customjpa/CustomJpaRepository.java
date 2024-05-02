package com.poluectov.rvproject.repository.customjpa;

import com.poluectov.rvproject.model.IdentifiedEntity;
import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.repository.ICommonRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.repository.jpa.JpaMarkerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class CustomJpaRepository<Entity extends IdentifiedEntity, ID> implements ICommonRepository<Entity, ID> {
    JpaRepository<Entity, ID> jpaMarkerRepository;
    EntityManager entityManager;

    private final String ENTITY_NAME;

    public CustomJpaRepository(
            JpaRepository<Entity, ID> jpaMarkerRepository,
            EntityManager entityManager,
            String entityName) {
        this.entityManager = entityManager;
        this.jpaMarkerRepository = jpaMarkerRepository;
        this.ENTITY_NAME = entityName;
    }

    public List<Entity> findAll() throws EntityNotFoundException {
        return jpaMarkerRepository.findAll();
    }

    @Override
    public <S extends Entity> S save(Entity entity) throws EntityNotFoundException {
        return jpaMarkerRepository.save((S) entity);
    }


    @Transactional
    public void deleteById(ID id) throws EntityNotFoundException {
        // Create the JPQL query
        String jpql = "DELETE FROM " + ENTITY_NAME + " e WHERE e.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();

        // Check if any rows were deleted
        if (deletedCount == 0) {
            throw new EntityNotFoundException(ENTITY_NAME + " with id " + id + " not found");
        }
    }

    public Optional<Entity> findById(ID aLong) throws EntityNotFoundException {
        return jpaMarkerRepository.findById(aLong);
    }


}
