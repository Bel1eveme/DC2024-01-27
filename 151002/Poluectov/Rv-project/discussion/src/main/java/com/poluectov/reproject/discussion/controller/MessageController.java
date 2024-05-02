package com.poluectov.reproject.discussion.controller;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.repository.MessageRepository;
import com.poluectov.reproject.discussion.repository.exception.EntityNotFoundException;
import com.poluectov.reproject.discussion.service.CommonRestService;
import com.poluectov.reproject.discussion.service.MessageService;
import com.poluectov.reproject.discussion.utils.dtoconverter.modelassembler.MessageModelAssembler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/messages")
public class MessageController {
    MessageService service;
    Function<MessageResponseTo, EntityModel<MessageResponseTo>> assembler;
    public MessageController(
            MessageService service,
            MessageModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler::toModel;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseTo> all() {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newEntity(@RequestBody MessageRequestTo request, HttpServletRequest httpServletRequest) {

        request.setCountry(httpServletRequest.getLocale().getCountry());

        Optional<MessageResponseTo> response = service.create(request);

        EntityModel<MessageResponseTo> entityModel =  assembler.apply(response.orElse(null));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> one(@PathVariable("id") Long id) {
        EntityModel<MessageResponseTo> entityModel = assembler.apply(service.one(id).orElseThrow(() -> new EntityNotFoundException("Entity not found")));

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
