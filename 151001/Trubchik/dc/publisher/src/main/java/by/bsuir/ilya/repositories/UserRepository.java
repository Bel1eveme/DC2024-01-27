package by.bsuir.ilya.repositories;

import by.bsuir.ilya.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findById(long id);

    List<User> findAll();

    User save(User user);

    void deleteById(long id);
}
