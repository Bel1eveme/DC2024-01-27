package by.bsuir.romankokarev.dao;

import java.util.List;

public interface Dao<T> {
    T create(T entity);
    T read(int id);
    List<T> readAll();
    T update(T entity, int id);
    boolean delete(int id);
}
