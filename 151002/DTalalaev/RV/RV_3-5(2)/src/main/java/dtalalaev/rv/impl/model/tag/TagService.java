package dtalalaev.rv.impl.model.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {


    @Autowired
    private TagRepository tagRepository;

    public TagResponseTo findOne(BigInteger id) throws ResponseStatusException {
        if(!tagRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag Not Found");
        }
        Optional<Tag> tag = tagRepository.findById(id);
        return new TagResponseTo(tag.get().getId(), tag.get().getName());
    }


    public List<Tag> findAll() {
        return (List<Tag>) tagRepository.findAll();
    }

    public TagResponseTo create(TagRequestTo dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        tagRepository.save(tag);
        Tag tag1 = tagRepository.findById(tag.getId()).get();
        TagResponseTo tagResponseTo = new TagResponseTo(tag1.getId(), tag1.getName());
        return tagResponseTo;
    }
    public TagResponseTo update(TagRequestTo dto) throws ResponseStatusException {
        if(!tagRepository.existsById(dto.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag Not Found");
        }
        Tag tagWas = tagRepository.findById(dto.getId()).get();
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName() == null ? tagWas.getName() : dto.getName());
        tagRepository.save(tag);
        Tag tag1 = tagRepository.findById(tag.getId()).get();
        TagResponseTo tagResponseTo = new TagResponseTo(tag1.getId(), tag1.getName());
        return tagResponseTo;
    }

    public void delete(BigInteger id) throws ResponseStatusException {
        if(!tagRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag Not Found");
        }
        tagRepository.deleteById(id);
    }
}

