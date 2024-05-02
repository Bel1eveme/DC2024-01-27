package com.poluectov.rvproject.controller.crud;

import com.poluectov.rvproject.model.IdentifiedEntity;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.service.CommonRestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class CommonRESTController<Entity extends IdentifiedEntity,
        Request extends IdentifiedEntity,
        Response extends IdentifiedEntity> {

    CommonRestService<Entity, Request, Response, Long> service;
    Function<Response, EntityModel<Response>> assembler;

    public CommonRESTController(CommonRestService<Entity, Request, Response, Long> service,
                                Function<Response, EntityModel<Response>> assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Response> all(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newEntity(@RequestBody Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<Response> response = service.create(request);

        EntityModel<Response> entityModel =  assembler.apply(response.orElse(null));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> one(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        EntityModel<Response> entityModel = assembler.apply(service.one(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id " + id + " not found")));

        return ResponseEntity
                .ok()
                .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Response updateEntity(@RequestBody Request request,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) {
        Long id = request.getId();

        Optional<Response> user = service.update(id, request);

        return user.orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable("id") Long id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
