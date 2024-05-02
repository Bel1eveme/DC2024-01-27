package com.example.publicator.controller;

import com.example.publicator.dto.NoteRequestTo;
import com.example.publicator.dto.NoteResponseTo;
import com.example.publicator.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class NoteController {

    WebClient client = WebClient.create();
    private String uriBase = "http://localhost:24130/api/v1.0/";

    @PostMapping(value = "notes")
    public ResponseEntity<?> create(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody NoteRequestTo noteRequestTo) {
        NoteResponseTo note = client.post().uri(uriBase + "notes").header(HttpHeaders.ACCEPT_LANGUAGE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(noteRequestTo), NoteRequestTo.class).retrieve().bodyToMono(NoteResponseTo.class).onErrorMap(NotFoundException.class, ex -> new NotFoundException(ex.getMessage(), ex.getStatus())).block();
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @GetMapping(value = "notes", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> read(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction)
    {
        final List<NoteResponseTo> list = client.get().uri(uriBase + "notes").retrieve().bodyToFlux(NoteResponseTo.class).collectList().block();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "notes/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") int id) {
        NoteResponseTo note = client.get().uri(uriBase + "notes/" + id).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                {
                    throw new NotFoundException("Note not found", 404);
                }
                )
                .bodyToMono(NoteResponseTo.class).block();
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @PutMapping(value = "notes")
    public ResponseEntity<?> update(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody NoteRequestTo noteRequestTo) {
        NoteResponseTo note = client.put().uri(uriBase + "notes").header(HttpHeaders.ACCEPT_LANGUAGE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(noteRequestTo), NoteRequestTo.class).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        {
                            throw new NotFoundException("Note not found", 404);
                        }
                )
                .bodyToMono(NoteResponseTo.class).block();
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @DeleteMapping(value = "notes/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        boolean isDeleted = Boolean.TRUE.equals(client.delete().uri(uriBase + "notes/" + id).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        {
                            throw new NotFoundException("Note not found", 404);
                        }
                )
                .bodyToMono(Boolean.class).block());
        return new ResponseEntity<>(isDeleted, HttpStatus.NO_CONTENT);
    }
}
