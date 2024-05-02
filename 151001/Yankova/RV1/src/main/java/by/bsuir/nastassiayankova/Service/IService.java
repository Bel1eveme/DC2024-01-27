package by.bsuir.nastassiayankova.Service;

import java.util.List;

public interface IService<T, K> {
    List<T> getAll();

    T update(K requestTo);

    T get(long id);

    T delete(long id);

    T add(K requestTo);

}

