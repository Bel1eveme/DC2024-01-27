package org.example.publisher.impl.note.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.api.kafka.producer.NoteProducer;
import org.example.publisher.impl.note.dto.NoteAddedResponseTo;
import org.example.publisher.impl.note.dto.NoteRequestTo;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.example.publisher.impl.note.mapper.Impl.NoteMapperImpl;
import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.issue.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final IssueRepository issueRepository;

    private final NoteMapperImpl noteMapper;
    private final NoteProducer noteProducer;

    private final String ENTITY_NAME = "note";

    private final String NOTE_COMMENT = "http://localhost:24130/api/v1.0/notes";

    public List<NoteResponseTo> getNotes(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoteResponseTo[]> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<NoteResponseTo[]> response = restTemplate.exchange(NOTE_COMMENT, HttpMethod.GET, requestEntity, NoteResponseTo[].class);
        return new ArrayList<>(List.of(Objects.requireNonNull(response.getBody())));
    }

    public NoteResponseTo getNoteById(BigInteger id) throws EntityNotFoundException, InterruptedException {
	try {
            return noteProducer.sendNote("get", id.toString(), true);
        } catch (RuntimeException e)
        {
            throw new EntityNotFoundException("note", id);
        }
    }

    public NoteAddedResponseTo saveNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException, InterruptedException {
        Optional<Issue> issue = issueRepository.findById(noteTO.getIssueId());
        if (issue.isEmpty()){
            throw new EntityNotFoundException("issue", noteTO.getIssueId());
        }
        try {
            noteTO.setId(generateId(16));
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(noteTO);
            noteProducer.sendNote("post", json, false);
            return new NoteAddedResponseTo(noteTO.getId(), noteTO.getIssueId(), noteTO.getContent(), "pending");
        } catch (DataIntegrityViolationException | JsonProcessingException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    public NoteResponseTo updateNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException, JsonProcessingException, InterruptedException {
        Optional<Issue> issue = issueRepository.findById(noteTO.getIssueId());
        if (issue.isEmpty()){
            throw new EntityNotFoundException("issue", noteTO.getIssueId());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(noteTO);
            NoteResponseTo note = noteProducer.sendNote("put", json, true);
            return note;
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    public void deleteNote(BigInteger id) throws EntityNotFoundException, InterruptedException {
	try {
            noteProducer.sendNote("get", id.toString(), true);
        } catch (RuntimeException e)
        {
            throw new EntityNotFoundException("note", id);
        }

        noteProducer.sendNote("delete", id.toString(), false);
    }

    public static BigInteger generateId(int numBits) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[numBits / 8];
        secureRandom.nextBytes(bytes);
        return new BigInteger(1, bytes);
    }
}
