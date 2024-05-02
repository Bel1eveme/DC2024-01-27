package com.poluectov.rvproject.service;

import com.poluectov.rvproject.model.IdentifiedEntity;
import com.poluectov.rvproject.repository.ICommonRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.utils.dtoconverter.DtoConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpRequest;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@Validated
public abstract class CommonRestService<Entity extends IdentifiedEntity, Request extends IdentifiedEntity, Response extends IdentifiedEntity, ID> {
    protected ICommonRepository<Entity, ID> crudRepository;
    protected DtoConverter<Request, Entity> dtoConverter;

    public CommonRestService(ICommonRepository<Entity, ID> crudRepository,
                             DtoConverter<Request, Entity> dtoConverter) {
        this.crudRepository = crudRepository;
        this.dtoConverter = dtoConverter;
    }

    protected abstract Optional<Response> mapResponseTo(Entity entity);

    protected abstract void update(Entity one, Entity found);

    public List<Response> all() {
        List<Response> all = new ArrayList<>();
        for (Entity one : crudRepository.findAll()) {
            Optional<Response> to = mapResponseTo(one);
            to.ifPresent(all::add);
        }
        return all;
    }

    public Optional<Response> one(ID id) {


        Optional<Entity> one = crudRepository.findById(id);
        if (one.isPresent()) {
            return mapResponseTo(one.get());
        }
        return Optional.empty();
    }

    public Optional<Response> create(@Valid Request request) {
        Entity one = dtoConverter.convert(request);
        Entity entity = crudRepository.save(one);
        return mapResponseTo(entity);
    }

    @Transactional
    public Optional<Response> update(ID id, @Valid Request request) {
        //repository updates by id
        Entity newUser = dtoConverter.convert(request);

        Optional<Entity> found = crudRepository.findById(id);

        if (found.isEmpty()) {
            throw new EntityNotFoundException(getEntityName() + " with id " + id + " not found");
        }

        Entity one = found.get();
        update(one, newUser);

        Entity entity = crudRepository.save(one);
        return mapResponseTo(entity);
    }
    @Transactional
    public void delete(ID id) throws EntityNotFoundException {
        try {
            crudRepository.deleteById(id);
        }catch (Exception e){
            throw new EntityNotFoundException(getEntityName() + " with id " + id + " not found");
        }
    }

    protected String getEntityName(){
        String className = this.getClass().getSimpleName();
        className = className.replace("Service", "");
        for(int i = className.length() - 1; i >= 0; i--){
            if(Character.isUpperCase(className.charAt(i))){
                className = className.substring(i);
                break;
            }
        }
        return className;
    }

}
