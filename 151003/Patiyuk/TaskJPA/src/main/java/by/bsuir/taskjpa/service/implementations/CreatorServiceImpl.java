package by.bsuir.taskrest.service.implementations;

import by.bsuir.taskrest.dto.request.CreatorRequestTo;
import by.bsuir.taskrest.dto.response.CreatorResponseTo;
import by.bsuir.taskrest.exception.CreateEntityException;
import by.bsuir.taskrest.exception.EntityNotFoundException;
import by.bsuir.taskrest.mapper.CreatorMapper;
import by.bsuir.taskrest.repository.CreatorRepository;
import by.bsuir.taskrest.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final CreatorMapper mapper;

    @Override
    public List<CreatorResponseTo> getAllCreators(PageRequest pageRequest) {
        return creatorRepository.findAll(pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    public CreatorResponseTo getCreatorById(Long id) {
        return creatorRepository.findById(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Creator with id " + id + " not found"));
    }

    @Override
    public CreatorResponseTo getCreatorByStoryId(Long id) {
        return creatorRepository.findByStories_Id(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Creator with story id " + id + " not found"));
    }

    @Override
    public CreatorResponseTo createCreator(CreatorRequestTo creator) {
        return Optional.of(creator)
                .map(mapper::toEntity)
                .map(creatorRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new CreateEntityException("Creator with id " + creator.id() + " not created"));
    }

    @Override
    public CreatorResponseTo updateCreator(CreatorRequestTo creator) {
        return creatorRepository.findById(creator.id())
                .map(creatorEntity -> mapper.updateEntity(creatorEntity, creator))
                .map(creatorRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Creator with id " + creator.id() + " not found"));
    }

    @Override
    public CreatorResponseTo updateCreator(Long id, CreatorRequestTo request) {
        return creatorRepository.findById(id)
                .map(creator -> mapper.updateEntity(creator, request))
                .map(creatorRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Creator with id " + id + " not found"));
    }

    @Override
    public void deleteCreator(Long id) {
        creatorRepository.findById(id)
                .ifPresentOrElse(creatorRepository::delete, () -> {
                    throw new EntityNotFoundException("Creator with id " + id + " not found");
                });
    }
}
