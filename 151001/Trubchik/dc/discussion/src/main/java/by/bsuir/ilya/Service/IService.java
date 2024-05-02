package by.bsuir.ilya.Service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IService<T, K> {


    public List<T> getAll();

    public T update(K requestTo);

    public T getById(long id);

    public boolean deleteById(long id);

    public ResponseEntity<T> add(K requestTo);

}

