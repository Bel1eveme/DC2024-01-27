package dtalalaev.rv.impl.model.post;

import dtalalaev.rv.impl.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StoryRepository storyRepository;

    public PostResponseTo findOne(BigInteger id) throws ResponseStatusException {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        Optional<Post> post = postRepository.findById(id);
        return new PostResponseTo(post.get().getId(), post.get().getStoryId(), post.get().getContent());
    }

    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }

    public PostResponseTo create(PostRequestTo dto) {
        if(!storyRepository.existsById(dto.getStoryId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Story not found");
        }
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setStoryId(dto.getStoryId());
        postRepository.save(post);
        Post post1 = postRepository.findById(post.getId()).get();
        PostResponseTo postResponseTo = new PostResponseTo(post1.getId(), post1.getStoryId(), post1.getContent());
        return postResponseTo;
    }

    public PostResponseTo update(PostRequestTo dto) throws ResponseStatusException {
        if (!postRepository.existsById(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        Post postWas = postRepository.findById(dto.getId()).get();
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent() == null ? postWas.getContent() : dto.getContent());
        post.setStoryId(dto.getStoryId() == null ? postWas.getStoryId() : dto.getStoryId());
        postRepository.save(post);
        Post post1 = postRepository.findById(post.getId()).get();
        PostResponseTo postResponseTo = new PostResponseTo(post1.getId(), post1.getStoryId(), post1.getContent());
        return postResponseTo;
    }

    public void delete(BigInteger id) throws ResponseStatusException {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }
        postRepository.deleteById(id);
    }
}
