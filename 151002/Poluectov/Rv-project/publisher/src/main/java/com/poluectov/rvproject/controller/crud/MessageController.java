package com.poluectov.rvproject.controller.crud;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.dto.message.MessageResponseTo;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.service.message.MessageService;
import com.poluectov.rvproject.utils.modelassembler.MessageModelAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/messages")
public class MessageController extends CommonRESTController<Message, MessageRequestTo, MessageResponseTo> {
    public MessageController(@Qualifier("messageService") MessageService service,
                             MessageModelAssembler assembler) {
        super(service, assembler::toModel);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newEntity(@RequestBody MessageRequestTo request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        request.setCountry(httpServletRequest.getLocale().getCountry());

        return super.newEntity(request, httpServletRequest, httpServletResponse);
    }
}
