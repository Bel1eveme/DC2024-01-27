package com.example.dc_project.dao;

import java.util.Optional;

public interface IRepository<T, A> {
    Optional<A> getById(T id);
    Iterable<A> getAll();
    Optional<A> save (A entity);
    Optional<A> update(A entity);
    boolean remove(A entity);
    boolean removeById(T id);
}