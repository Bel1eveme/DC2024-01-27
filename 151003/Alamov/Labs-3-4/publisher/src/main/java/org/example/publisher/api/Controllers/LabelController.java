package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.note.dto.NoteRequestTo;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.example.publisher.impl.label.*;
import org.example.publisher.impl.label.Service.LabelService;
import org.example.publisher.impl.label.dto.LabelRequestTo;
import org.example.publisher.impl.label.dto.LabelResponseTo;
import org.example.publisher.impl.label.mapper.Impl.LabelMapperImpl;
import org.example.publisher.impl.label.mapper.LabelMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1.0/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LabelResponseTo> getLabels() {
        return labelService.getLabels();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelResponseTo getLabelById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return labelService.getLabelById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponseTo saveLabel(@Valid @RequestBody LabelRequestTo labelTo) throws DuplicateEntityException, EntityNotFoundException {
        return labelService.saveLabel(labelTo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public LabelResponseTo updateNote(@Valid @RequestBody LabelRequestTo labelTo) throws DuplicateEntityException, EntityNotFoundException {
        return labelService.updateLabel(labelTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable BigInteger id) throws EntityNotFoundException {
        labelService.deleteLabelById(id);
    }

}
