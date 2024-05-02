package dtalalaev.rv.impl.model.story;

import dtalalaev.rv.impl.model.creator.CreatorRepository;
import dtalalaev.rv.impl.model.creator.CreatorResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.CannotReadScriptException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryService {


    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private CreatorRepository creatorRepository;

    public StoryResponseTo findOne(BigInteger id) throws ResponseStatusException {
        if(!storyRepository.existsById(id)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story Not Found");
        }
        Optional<Story> story = storyRepository.findById(id);
        return new StoryResponseTo(story.get().getId(), story.get().getCreatorId(), story.get().getTitle(), story.get().getContent(), story.get().getCreated(), story.get().getModified());
    }


    public List<Story> findAll() {
        return (List<Story>) storyRepository.findAll();
    }

    public StoryResponseTo create(StoryRequestTo dto) throws ResponseStatusException{
        if(!creatorRepository.existsById(dto.getCreatorId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creator not found");
        }
        Story story = new Story();
        story.setCreatorId(dto.getCreatorId());
        story.setTitle(dto.getTitle());
        story.setContent(dto.getContent());
        try {
            storyRepository.save(story);
        } catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Story with this title already exists");
        }
        Story story1 = storyRepository.findById(story.getId()).get();
        StoryResponseTo storyResponseTo = new StoryResponseTo(story1.getId(), story1.getCreatorId(), story1.getTitle(), story1.getContent(), story1.getCreated(), story1.getModified());
        return storyResponseTo;
    }

    public StoryResponseTo update(StoryRequestTo dto) throws ResponseStatusException {
        if(!storyRepository.existsById(dto.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story Not Found");
        }
        Story storyWas = storyRepository.findById(dto.getId()).get();
        Story story = new Story();
        story.setId(dto.getId());
        story.setCreatorId(dto.getCreatorId() == null ? storyWas.getCreatorId() : dto.getCreatorId());
        story.setTitle(dto.getTitle() == null ? storyWas.getTitle() : dto.getTitle());
        story.setContent(dto.getContent() == null ? storyWas.getContent() : dto.getContent());
        story.setModified(new Date());
        story.setCreated(storyWas.getCreated());
        storyRepository.save(story);
        Story story1 = storyRepository.findById(story.getId()).get();
        StoryResponseTo storyResponseTo = new StoryResponseTo(story1.getId(), story1.getCreatorId(), story1.getTitle(), story1.getContent(), story1.getCreated(), story1.getModified());
        return storyResponseTo;
    }

    public void delete(BigInteger id) throws ResponseStatusException {
        if(!storyRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story Not Found");
        }
        storyRepository.deleteById(id);
    }
}
