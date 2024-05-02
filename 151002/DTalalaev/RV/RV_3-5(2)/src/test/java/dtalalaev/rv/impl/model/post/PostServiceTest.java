package dtalalaev.rv.impl.model.post;

import com.mysql.cj.protocol.Protocol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    void testFindOne() {
        BigInteger id = BigInteger.valueOf(1);
        Post post = new Post();
        post.setId(id);
        post.setStoryId(BigInteger.valueOf(1));
        post.setContent("Test content");

        // Mock the behavior of PostRepository
        when(postRepository.existsById(id)).thenReturn(true);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // Call the method and verify the behavior
        PostResponseTo response = postService.findOne(id);

        // Verify that the correct methods were called
        verify(postRepository).existsById(id);
        verify(postRepository).findById(id);

        // Verify the response
        assertEquals(post.getId(), response.getId());
        assertEquals(post.getStoryId(), response.getStoryId());
        assertEquals(post.getContent(), response.getContent());
    }

    @Test
    void testFindOneNotFound() {
        BigInteger id = BigInteger.valueOf(2);

        // Mock the behavior of PostRepository
        when(postRepository.existsById(id)).thenReturn(false);

        // Call the method and verify the behavior
        assertThrows(ResponseStatusException.class, () -> postService.findOne(id));

        // Verify that the correct methods were called
        verify(postRepository).existsById(id);
        verify(postRepository, never()).findById(id);
    }

    @Test
    void testFindAll() {
        // Create a list of posts
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(BigInteger.valueOf(1));
        post1.setStoryId(BigInteger.valueOf(1));
        post1.setContent("Test content 1");
        Post post2 = new Post();
        post2.setId(BigInteger.valueOf(2));
        post2.setStoryId(BigInteger.valueOf(2));
        post2.setContent("Test content 2");
        posts.add(post1);
        posts.add(post2);

        // Mock the behavior of PostRepository
        when(postRepository.findAll()).thenReturn(posts);

        // Call the method and verify the behavior
        List<Post> postList = postService.findAll();

        // Verify that the correct method was called
        verify(postRepository).findAll();

        // Verify the response
        assertEquals(posts.size(), postList.size());
        assertEquals(posts.get(0).getId(), postList.get(0).getId());
        assertEquals(posts.get(0).getStoryId(), postList.get(0).getStoryId());
        assertEquals(posts.get(0).getContent(), postList.get(0).getContent());
        assertEquals(posts.get(1).getId(), postList.get(1).getId());
        assertEquals(posts.get(1).getStoryId(), postList.get(1).getStoryId());
        assertEquals(posts.get(1).getContent(), postList.get(1).getContent());
    }

    @Test
    void testDelete() {
        BigInteger id = BigInteger.valueOf(1);

        // Mock the behavior of PostRepository
        when(postRepository.existsById(id)).thenReturn(true);

        // Call the method and verify the behavior
        postService.delete(id);

        // Verify that the correct method was called
        verify(postRepository).existsById(id);
        verify(postRepository).deleteById(id);
    }



    @Test
    void testUpdate() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateNoneVariant() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateWithNewContext() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateIncorrectParams() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateCorrectParams() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateDuplicateParams() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(4));
        request.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(BigInteger.valueOf(4));
        existingPost.setContent("Original content");
        existingPost.setStoryId(BigInteger.valueOf(4));

        when(postRepository.existsById(request.getId())).thenReturn(true);
        when(postRepository.findById(request.getId())).thenReturn(Optional.of(existingPost));

        PostResponseTo response = postService.update(request);

        verify(postRepository).save(any(Post.class));
        assertEquals(existingPost.getId(), response.getId());
        assertEquals(existingPost.getStoryId(), response.getStoryId());
    }

    @Test
    void testUpdateNotFound() {
        PostRequestTo request = new PostRequestTo();
        request.setId(BigInteger.valueOf(5));
        request.setContent("Updated content");

        when(postRepository.existsById(request.getId())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> postService.update(request));
    }

    @Test
    void testDeleteNotFound() {
        BigInteger id = BigInteger.valueOf(6);

        when(postRepository.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> postService.delete(id));
    }

    @Test
    void testDeleteNone() {
        BigInteger id = BigInteger.valueOf(7);

        when(postRepository.existsById(id)).thenReturn(true);

        postService.delete(id);

        verify(postRepository).deleteById(id);
    }
}