package org.education.controller;

import org.education.bean.Label;
import org.education.bean.dto.LabelRequestTo;
import org.education.bean.dto.LabelResponseTo;
import org.education.exception.AlreadyExists;
import org.education.exception.ErrorResponse;
import org.education.exception.IncorrectValuesException;
import org.education.exception.NoSuchLabel;
import org.education.service.LabelService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/labels")
public class LabelController {

    private final LabelService labelService;

    private final ModelMapper modelMapper;

    @Autowired
    public LabelController(LabelService labelService, ModelMapper modelMapper) {
        this.labelService = labelService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<LabelResponseTo>> getAllMLabels(){
        return new ResponseEntity<>(labelService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LabelResponseTo> createLabel(@RequestBody @Valid LabelRequestTo labelRequestTo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new IncorrectValuesException("Incorrect input values");
        }
        return new ResponseEntity<>(labelService.create(modelMapper.map(labelRequestTo, Label.class)), HttpStatus.valueOf(201));
    }

    @PutMapping
    public ResponseEntity<LabelResponseTo> updateLabel(@RequestBody @Valid LabelRequestTo markerRequestTo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new IncorrectValuesException("Incorrect input values");
        }
        return new ResponseEntity<>(labelService.update(modelMapper.map(markerRequestTo, Label.class)), HttpStatus.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLabel(@PathVariable int id){
        labelService.delete(id);
        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseTo> getLabel(@PathVariable int id){
        return new ResponseEntity<>(modelMapper.map(labelService.getById(id), LabelResponseTo.class), HttpStatus.valueOf(200));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(NoSuchLabel exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(IncorrectValuesException exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(AlreadyExists exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), 403),HttpStatus.valueOf(403));
    }
}