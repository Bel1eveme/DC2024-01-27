package by.bsuir.publisherservice.service;

import by.bsuir.publisherservice.dto.request.CreatorRequestTo;
import by.bsuir.publisherservice.dto.response.CreatorResponseTo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CreatorService {
    List<CreatorResponseTo> getAllCreators(PageRequest pageRequest);
    CreatorResponseTo getCreatorById(Long id);
    CreatorResponseTo getCreatorByStoryId(Long id);
    CreatorResponseTo createCreator(CreatorRequestTo creator);
    CreatorResponseTo updateCreator(CreatorRequestTo creator);
    CreatorResponseTo updateCreator(Long id, CreatorRequestTo creator);
    void deleteCreator(Long id);
}
