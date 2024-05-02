package by.bsuir.publisherservice.service.implementations;

import by.bsuir.publisherservice.dto.request.CreatorRequestTo;
import by.bsuir.publisherservice.dto.response.CreatorResponseTo;
import by.bsuir.publisherservice.exception.CreateEntityException;
import by.bsuir.publisherservice.exception.EntityNotFoundException;
import by.bsuir.publisherservice.mapper.CreatorMapper;
import by.bsuir.publisherservice.repository.CreatorRepository;
import by.bsuir.publisherservice.service.CreatorService;
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
        return updateCreator(creator.id(), creator);
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
