package com.poluectov.rvproject.utils.modelassembler;

import com.poluectov.rvproject.controller.crud.IssueController;
import com.poluectov.rvproject.controller.crud.MarkerController;
import com.poluectov.rvproject.controller.crud.UserController;
import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IssueModelAssembler implements RepresentationModelAssembler<IssueResponseTo, EntityModel<IssueResponseTo>> {
    @Override
    public EntityModel<IssueResponseTo> toModel(IssueResponseTo entity) {
            EntityModel<IssueResponseTo> model = EntityModel.of(entity,
                linkTo(methodOn(IssueController.class).one(entity.getId(), null, null)).withSelfRel(),
                linkTo(methodOn(UserController.class).one(entity.getUserId(), null, null)).withRel("users"),
                linkTo(methodOn(IssueController.class).all(null, null)).withRel("issues"));

        if (entity.getMarkers() != null) {
            for (Long marker : entity.getMarkers()) {
                model.add(linkTo(methodOn(MarkerController.class).one(marker, null, null)).withRel("markers"));
            }
        }

        return model;
    }
}
