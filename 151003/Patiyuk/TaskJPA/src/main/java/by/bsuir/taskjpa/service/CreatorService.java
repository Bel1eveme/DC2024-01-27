package by.bsuir.taskrest.service;

import by.bsuir.taskrest.dto.request.CreatorRequestTo;
import by.bsuir.taskrest.dto.response.CreatorResponseTo;
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
