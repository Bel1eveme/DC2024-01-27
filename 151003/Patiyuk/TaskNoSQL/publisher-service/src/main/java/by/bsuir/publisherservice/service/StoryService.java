package by.bsuir.publisherservice.service;

import by.bsuir.publisherservice.dto.request.StoryRequestTo;
import by.bsuir.publisherservice.dto.response.StoryResponseTo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface StoryService {
    List<StoryResponseTo> getAllStories(PageRequest pageRequest);
    StoryResponseTo getStoryById(Long id);
    StoryResponseTo createStory(StoryRequestTo story);
    StoryResponseTo updateStory(StoryRequestTo story);
    StoryResponseTo updateStory(Long id, StoryRequestTo story);
    void deleteStory(Long id);
}
