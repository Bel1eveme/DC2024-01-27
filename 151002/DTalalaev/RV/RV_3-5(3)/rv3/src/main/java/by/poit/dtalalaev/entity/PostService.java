package by.poit.dtalalaev.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
@CacheConfig(cacheNames = "postCache")
public class PostService {


    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }



    @Cacheable(cacheNames = "posts")
    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }


    @Caching(evict = { @CacheEvict(cacheNames = "posts", key = "#id"),
            @CacheEvict(cacheNames = "posts", allEntries = true) })
    public void delete(BigInteger id) throws ResponseStatusException {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        postRepository.deleteById(id);
    }

    @Cacheable(cacheNames = "posts", key = "#id", unless = "#result == null")
    public PostResponseTo findOne(BigInteger id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        return new PostResponseTo(post.get());
    }

    @CacheEvict(cacheNames = "posts", allEntries = true)
    public PostResponseTo create(PostRequestTo dto) {

        Post post = new Post();
        post.setContent(dto.getContent());
        post.setStoryId(dto.getStoryId());
        postRepository.save(post);
        return new PostResponseTo(post);

    }

    @CacheEvict(cacheNames = "posts", allEntries = true)
    public PostResponseTo update(PostRequestTo dto) {

        Optional<Post> post = postRepository.findById(dto.getId());
        if (!post.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        post.get().setContent(dto.getContent());
        postRepository.save(post.get());
        return new PostResponseTo(post.get());

    }
}
