package by.bsuir.taskrest.service.implementations;

import by.bsuir.taskrest.dto.request.StoryRequestTo;
import by.bsuir.taskrest.dto.response.StoryResponseTo;
import by.bsuir.taskrest.exception.EntityNotFoundException;
import by.bsuir.taskrest.mapper.StoryMapper;
import by.bsuir.taskrest.repository.CreatorRepository;
import by.bsuir.taskrest.repository.StoryRepository;
import by.bsuir.taskrest.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final CreatorRepository creatorRepository;
    private final StoryMapper mapper;

    @Override
    public List<StoryResponseTo> getAllStories(PageRequest pageRequest) {
        return storyRepository.findAll(pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    public StoryResponseTo getStoryById(Long id) {
        return storyRepository.findById(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + id + " not found"));
    }

    @Override
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
    public StoryResponseTo updateStory(StoryRequestTo story) {
        return storyRepository.findById(story.id())
                .map(entity -> mapper.updateEntity(entity, story))
                .map(storyRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + story.id() + " not found"));
    }

    @Override
    public StoryResponseTo updateStory(Long id, StoryRequestTo story) {
        return storyRepository.findById(id)
                .map(entity -> mapper.updateEntity(entity, story))
                .map(storyRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Story with id " + id + " not found"));
    }

    @Override
    public void deleteStory(Long id) {
        storyRepository.findById(id)
                .ifPresentOrElse(storyRepository::delete, () -> {
                    throw new EntityNotFoundException("Story with id " + id + " not found");
                });
    }
}
