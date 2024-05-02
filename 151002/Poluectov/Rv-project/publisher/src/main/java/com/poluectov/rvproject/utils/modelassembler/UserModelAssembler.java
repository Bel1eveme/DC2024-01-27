package com.poluectov.rvproject.utils.modelassembler;

import com.poluectov.rvproject.controller.crud.UserController;
import com.poluectov.rvproject.dto.user.UserResponseTo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseTo, EntityModel<UserResponseTo>> {

    @Override
    public EntityModel<UserResponseTo> toModel(UserResponseTo employee) {

        return EntityModel.of(employee, //
                linkTo(methodOn(UserController.class).one(employee.getId(), null, null)).withSelfRel(),
                linkTo(methodOn(UserController.class).all(null, null)).withRel("users"));
    }
}
