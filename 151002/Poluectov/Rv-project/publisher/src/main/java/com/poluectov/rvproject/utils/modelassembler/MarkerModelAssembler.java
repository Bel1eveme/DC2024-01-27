package com.poluectov.rvproject.utils.modelassembler;

import com.poluectov.rvproject.controller.crud.MarkerController;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class MarkerModelAssembler implements RepresentationModelAssembler<MarkerResponseTo, EntityModel<MarkerResponseTo>> {
    @Override
    public EntityModel<MarkerResponseTo> toModel(MarkerResponseTo entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(MarkerController.class).one(entity.getId(), null, null)).withSelfRel(),
                linkTo(methodOn(MarkerController.class).all(null, null)).withRel("markers"));
    }
}
