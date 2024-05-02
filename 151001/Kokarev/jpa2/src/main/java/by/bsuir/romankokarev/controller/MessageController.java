package by.bsuir.romankokarev.controller;

import by.bsuir.romankokarev.service.MessageService;
import by.bsuir.romankokarev.dto.MessageRequestTo;
import by.bsuir.romankokarev.dto.MessageResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping(value = "messages")
    public ResponseEntity<?> create(@RequestBody MessageRequestTo messageRequestTo) {
        MessageResponseTo message = messageService.create(messageRequestTo);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "messages", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> read(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction) {
        final List<MessageResponseTo> list = messageService.readAll(pageInd, numOfElem, sortedBy, direction);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "messages/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") int id) {
        MessageResponseTo message = messageService.read(id);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(value = "messages")
    public ResponseEntity<?> update(@RequestBody MessageRequestTo messageRequestTo) {
        MessageResponseTo message = messageService.update(messageRequestTo, messageRequestTo.getId());

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "messages/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        messageService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
