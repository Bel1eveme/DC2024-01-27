package by.bsuir.ilya.Controllers;

import by.bsuir.ilya.Service.PostService;
import by.bsuir.ilya.dto.PostRequestTo;
import by.bsuir.ilya.dto.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseTo>> getAllPosts() {
        List<PostResponseTo> PostResponseToList = postService.getAllKafka();
        return new ResponseEntity<>(PostResponseToList, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable long id) {
        PostResponseTo postResponseTo = postService.getByIdKafka(id);
        return new ResponseEntity<>(postResponseTo, postResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseTo> createPost(@RequestBody PostRequestTo PostTo) {
        return postService.addKafka(PostTo);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> deletePost(@PathVariable long id) {
        return new ResponseEntity<>(null,postService.deleteByIdKafka(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/posts")
    public ResponseEntity<PostResponseTo> updatePost(@RequestBody PostRequestTo PostRequestTo) {
        PostResponseTo PostResponseTo = postService.update(PostRequestTo);
        return new ResponseEntity<>(PostResponseTo, PostResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
