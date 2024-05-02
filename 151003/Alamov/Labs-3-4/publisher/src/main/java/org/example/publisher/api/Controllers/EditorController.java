package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.editor.dto.EditorRequestTo;
import org.example.publisher.impl.editor.dto.EditorResponseTo;
import org.example.publisher.impl.editor.service.EditorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/editors")
public class EditorController {

    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<EditorResponseTo> getEditors() {
        return editorService.getEditors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    EditorResponseTo getEditorById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return editorService.getEditorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EditorResponseTo makeEditor(@Valid @RequestBody EditorRequestTo editorRequestTo) throws DuplicateEntityException {
        return editorService.createEditor(editorRequestTo);
    }

    @PutMapping
    EditorResponseTo updateEditor(@Valid @RequestBody EditorRequestTo editorRequestTo) throws EntityNotFoundException{
        return editorService.updateEditor(editorRequestTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    void deleteEditor(@PathVariable BigInteger id) throws EntityNotFoundException {
        editorService.deleteEditor(id);
    }
}
