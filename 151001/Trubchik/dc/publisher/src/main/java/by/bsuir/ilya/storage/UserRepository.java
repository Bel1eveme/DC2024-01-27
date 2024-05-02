package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class UserRepository implements InMemoryRepository<User> {

    Map<Long, User> userMemory = new ConcurrentHashMap<>();

    AtomicLong lastId = new AtomicLong();

    public UserRepository() {

    }

    @Override
    public User findById(long id) {
        User user = userMemory.get(id);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        for (Long key : userMemory.keySet()) {
            User user = userMemory.get(key);
            ;
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User deleteById(long id) {
        User result = userMemory.remove(id);
        return result;
    }

    @Override
    public boolean deleteAll() {
        userMemory.clear();
        return true;
    }

    @Override
    public User insert(User insertObject) {
        long id = lastId.getAndIncrement();
        insertObject.setId(id);
        userMemory.put(id, insertObject);
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, User replacingUser) {
        boolean status = userMemory.replace(id, userMemory.get(id), replacingUser);
        return status;
    }

    @Override
    public boolean update(User updatingValue) {
        boolean status = userMemory.replace(updatingValue.getId(), userMemory.get(updatingValue.getId()), updatingValue);
        return status;
    }

}
