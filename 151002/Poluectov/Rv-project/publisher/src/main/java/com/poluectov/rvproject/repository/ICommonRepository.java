package com.poluectov.rvproject.repository;

import com.poluectov.rvproject.model.IdentifiedEntity;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ICommonRepository<Entity extends IdentifiedEntity, ID> {

    List<Entity> findAll() throws EntityNotFoundException;

    /**
     * Saves new if id is null
     * @param
     */
    <S extends Entity> S save(Entity entity) throws EntityNotFoundException;

    void deleteById(ID id) throws EntityNotFoundException;

    Optional<Entity> findById(ID id) throws EntityNotFoundException;

}
