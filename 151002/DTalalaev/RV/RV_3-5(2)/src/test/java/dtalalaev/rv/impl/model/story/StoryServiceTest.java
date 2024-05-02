package dtalalaev.rv.impl.model.story;

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
class StoryServiceTest {

    @InjectMocks
    private StoryService storyService;

    @Mock
    private StoryRepository storyRepository;

    @Test
    void testFindOne() {
        BigInteger id = BigInteger.valueOf(1);
        Story story = new Story();
        story.setId(id);
        story.setContent("Test content");

        // Mock the behavior of StoryRepository
        when(storyRepository.existsById(id)).thenReturn(true);
        when(storyRepository.findById(id)).thenReturn(Optional.of(story));

        // Call the method and verify the behavior
        StoryResponseTo response = storyService.findOne(id);

        // Verify that the correct methods were called
        verify(storyRepository).existsById(id);
        verify(storyRepository).findById(id);

        // Verify the response
        assertEquals(story.getId(), response.getId());
        assertEquals(story.getContent(), response.getContent());
    }

    @Test
    void testFindOneNotFound() {
        BigInteger id = BigInteger.valueOf(2);

        // Mock the behavior of StoryRepository
        when(storyRepository.existsById(id)).thenReturn(false);

        // Call the method and verify the behavior
        assertThrows(ResponseStatusException.class, () -> storyService.findOne(id));

        // Verify that the correct methods were called
        verify(storyRepository).existsById(id);
        verify(storyRepository, never()).findById(id);
    }

    @Test
    void testFindAll() {
        // Create a list of storys
        List<Story> storys = new ArrayList<>();
        Story story1 = new Story();
        story1.setId(BigInteger.valueOf(1));
        story1.setContent("Test content 1");
        Story story2 = new Story();
        story2.setId(BigInteger.valueOf(2));
        story2.setContent("Test content 2");
        storys.add(story1);
        storys.add(story2);

        // Mock the behavior of StoryRepository
        when(storyRepository.findAll()).thenReturn(storys);

        // Call the method and verify the behavior
        List<Story> storyList = storyService.findAll();

        // Verify that the correct method was called
        verify(storyRepository).findAll();

        // Verify the response
        assertEquals(storys.size(), storyList.size());
        assertEquals(storys.get(0).getId(), storyList.get(0).getId());
        assertEquals(storys.get(0).getContent(), storyList.get(0).getContent());
        assertEquals(storys.get(1).getId(), storyList.get(1).getId());
        assertEquals(storys.get(1).getContent(), storyList.get(1).getContent());
    }

    @Test
    void testDelete() {
        BigInteger id = BigInteger.valueOf(1);

        // Mock the behavior of StoryRepository
        when(storyRepository.existsById(id)).thenReturn(true);

        // Call the method and verify the behavior
        storyService.delete(id);

        // Verify that the correct method was called
        verify(storyRepository).existsById(id);
        verify(storyRepository).deleteById(id);
    }






    @Test
    void testDeleteNotFound() {
        BigInteger id = BigInteger.valueOf(6);

        when(storyRepository.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> storyService.delete(id));
    }

    @Test
    void testDeleteNone() {
        BigInteger id = BigInteger.valueOf(7);

        when(storyRepository.existsById(id)).thenReturn(true);

        storyService.delete(id);

        verify(storyRepository).deleteById(id);
    }


    @Test
    void testDeleteIncorrectParams() {
        BigInteger id = BigInteger.valueOf(7);

        when(storyRepository.existsById(id)).thenReturn(true);

        storyService.delete(id);

        verify(storyRepository).deleteById(id);
    }
}