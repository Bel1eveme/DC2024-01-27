package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Post;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepository implements InMemoryRepository<Post> {
    private Map<Long, Post> postMemory = new ConcurrentHashMap<>();

    AtomicLong lastId = new AtomicLong();

    @Override
    public Post findById(long id) {
        Post post = postMemory.get(id);
        return post;
    }

    @Override
    public List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        for (Long key : postMemory.keySet()) {
            Post post = postMemory.get(key);
            ;
            postList.add(post);
        }
        return postList;
    }

    @Override
    public Post deleteById(long id) {
        Post result = postMemory.remove(id);
        return result;
    }

    @Override
    public boolean deleteAll() {
        postMemory.clear();
        return true;
    }

    @Override
    public Post insert(Post insertObject) {
        long id = lastId.getAndIncrement();
        insertObject.setId(id);
        postMemory.put(id, insertObject);
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Post updatingPost) {
        boolean status = postMemory.replace(id, postMemory.get(id), updatingPost);
        return status;
    }

    @Override
    public boolean update(Post updatingValue) {
        boolean status = postMemory.replace(updatingValue.getId(), postMemory.get(updatingValue.getId()), updatingValue);
        return status;
    }


}
