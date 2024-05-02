package by.bsuir.publisherservice.service.implementations;

import by.bsuir.publisherservice.dto.request.StoryRequestTo;
import by.bsuir.publisherservice.dto.response.StoryResponseTo;
import by.bsuir.publisherservice.exception.EntityNotFoundException;
import by.bsuir.publisherservice.mapper.StoryMapper;
import by.bsuir.publisherservice.repository.CreatorRepository;
import by.bsuir.publisherservice.repository.StoryRepository;
import by.bsuir.publisherservice.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final CreatorRepository creatorRepository;
    private final StoryMapper mapper;

    @Override
    @Cacheable(value = "stories")
    public List<StoryResponseTo> getAllStories(PageRequest pageRequest) {
        return new ArrayList<>(storyRepository.findAll(pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList());
    }

    @Override
    @Cacheable(value = "stories", key = "#id")
    public StoryResponseTo getStoryById(Long id) {
        return storyRepository.findById(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + id + " not found"));
    }

    @Override
    @CacheEvict(cacheNames = "stories", allEntries = true)
    public StoryResponseTo createStory(StoryRequestTo story) {
        return Optional.of(story)
                .map(request -> mapper.toEntity(request, creatorRepository.findById(story.creatorId())
                        .orElseThrow(() -> new EntityNotFoundException("Creator with id " + story.creatorId() + " not found"))))
                .map(storyRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + story.id() + " not created"));
    }

    @Override
    @CacheEvict(cacheNames = "stories", allEntries = true)
    public StoryResponseTo updateStory(StoryRequestTo story) {
        return updateStory(story.id(), story);
    }

    @Override
    @CacheEvict(cacheNames = "stories", allEntries = true)
    public StoryResponseTo updateStory(Long id, StoryRequestTo story) {
        return storyRepository.findById(id)
                .map(entity -> mapper.updateEntity(entity, story))
                .map(storyRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + id + " not found"));
    }

    @Override
    @CacheEvict(cacheNames = "stories", allEntries = true)
    public void deleteStory(Long id) {
        storyRepository.findById(id)
                .ifPresentOrElse(storyRepository::delete, () -> {
                    throw new EntityNotFoundException("Story with id " + id + " not found");
                });
    }
}
