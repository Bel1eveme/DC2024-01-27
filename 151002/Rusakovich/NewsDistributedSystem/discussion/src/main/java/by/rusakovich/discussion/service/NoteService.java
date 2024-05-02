package by.rusakovich.discussion.service;

import by.rusakovich.discussion.model.*;
import by.rusakovich.discussion.spi.dao.NoteRepository;
import by.rusakovich.discussion.error.exceptions.NotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NoteService implements INoteService {

    private NoteMapper mapper;
    private NoteRepository rep;

    @Override
    public NoteResponseTO readById(Long id) {
        var entity = rep.findById(id).stream().findFirst().orElseThrow(NotFound::new);
        return mapper.mapToResponse(entity);
    }

    @Override
    public List<NoteResponseTO> readAll() {
        return rep.findAll().stream()
                .map(mapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseTO create(NoteIternalRequestTO newEntity) {
        var mappedNewEntity= mapper.mapToEntity(newEntity);
        if(mappedNewEntity.getId() == null)
            mappedNewEntity.setId(CassandraIdGenerator.getId());
        var res = rep.save(mappedNewEntity);
        return mapper.mapToResponse(res);
    }

    @Override
    public NoteResponseTO update(NoteIternalRequestTO updatedEntity) {
        var entity = rep.findById(updatedEntity.id()).stream().findFirst().orElseThrow(NotFound::new);
        Note mappedEntityToUpdate = mapper.mapToEntity(updatedEntity);
        rep.deleteByCountryAndNewsIdAndId(entity.getCountry(), entity.getNewsId(), entity.getId());
        mappedEntityToUpdate.setCountry(entity.getCountry());
        var res = rep.save(mappedEntityToUpdate);
        return mapper.mapToResponse(res);
    }

    @Override
    public void deleteById(Long id) {
        var entity = rep.findById(id).stream().findFirst().orElseThrow(NotFound::new);
        rep.deleteByCountryAndNewsIdAndId(entity.getCountry(), entity.getNewsId(), id);
    }
}
