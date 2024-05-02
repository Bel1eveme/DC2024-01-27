package com.poluectov.rvproject.controller.crud;

import com.poluectov.rvproject.dto.user.UserRequestTo;
import com.poluectov.rvproject.dto.user.UserResponseTo;
import com.poluectov.rvproject.model.User;
import com.poluectov.rvproject.service.CommonRestService;
import com.poluectov.rvproject.utils.modelassembler.UserModelAssembler;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
public class UserController extends CommonRESTController<User, UserRequestTo, UserResponseTo> {


    public UserController(CommonRestService<User, UserRequestTo, UserResponseTo, Long> service, UserModelAssembler assembler) {
        super(service, assembler::toModel);
    }

}
