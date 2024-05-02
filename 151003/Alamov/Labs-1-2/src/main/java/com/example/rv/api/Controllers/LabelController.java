package com.example.rv.api.Controllers;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.impl.label.Service.LabelService;
import com.example.rv.impl.label.dto.LabelRequestTo;
import com.example.rv.impl.label.dto.LabelResponseTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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
