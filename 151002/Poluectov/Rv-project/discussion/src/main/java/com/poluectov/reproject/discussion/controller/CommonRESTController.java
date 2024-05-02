package com.poluectov.reproject.discussion.controller;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import com.poluectov.reproject.discussion.model.IdentifiedEntity;
import com.poluectov.reproject.discussion.service.CommonRestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpRequest;
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


public class CommonRESTController {

    CommonRestService service;
    Function<MessageResponseTo, EntityModel<MessageResponseTo>> assembler;

    public CommonRESTController(CommonRestService service,
                                Function<MessageResponseTo, EntityModel<MessageResponseTo>> assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseTo> all() {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newEntity(@RequestBody MessageRequestTo request) {
        Optional<MessageResponseTo> response = service.create(request);

        EntityModel<MessageResponseTo> entityModel =  assembler.apply(response.orElse(null));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> one(@PathVariable("id") Long id) {
        EntityModel<MessageResponseTo> entityModel = assembler.apply(service.one(id).orElse(null));

        return ResponseEntity
                .ok()
                .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseTo updateEntity(
            @RequestBody MessageRequestTo request) {
        Long id = request.getId();

        Optional<MessageResponseTo> user = service.update(id, request);

        return user.orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable("id") Long id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
