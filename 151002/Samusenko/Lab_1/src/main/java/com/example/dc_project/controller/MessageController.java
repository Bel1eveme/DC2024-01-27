package com.example.dc_project.controller;
import com.example.dc_project.model.request.MessageRequestTo;
import com.example.dc_project.model.response.MessageResponseTo;
import com.example.dc_project.service.MessageService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
@Data
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseTo findById(@Valid @PathVariable("id") Long id) {
        return messageService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseTo> findAll() {
        return messageService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseTo create(@Valid @RequestBody MessageRequestTo request) {
        return messageService.create(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseTo update(@Valid @RequestBody MessageRequestTo request) {
        return messageService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean removeById(@Valid @PathVariable("id") Long id) {
        return messageService.removeById(id);
    }
}

