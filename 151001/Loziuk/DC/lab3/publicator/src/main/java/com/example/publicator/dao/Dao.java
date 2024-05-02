package com.example.publicator.dao;

import java.util.List;

public interface Dao<T> {
    T create(T entity);

    List<T> readAll();

    T read(int id);

    T update(T entity, int id);

    boolean delete(int id);
}
