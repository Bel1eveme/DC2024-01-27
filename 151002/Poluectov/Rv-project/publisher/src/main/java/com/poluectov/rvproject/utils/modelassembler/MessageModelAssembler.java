package com.poluectov.rvproject.utils.modelassembler;

import com.poluectov.rvproject.controller.crud.MessageController;
import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.dto.message.MessageResponseTo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigInteger;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MessageModelAssembler implements RepresentationModelAssembler<MessageResponseTo, EntityModel<MessageResponseTo>> {
    @Override
    public EntityModel<MessageResponseTo> toModel(MessageResponseTo entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(MessageController.class).one(entity.getId(), null, null)).withSelfRel(),
                linkTo(methodOn(MessageController.class).all(null, null)).withRel("messages"));
    }
}
