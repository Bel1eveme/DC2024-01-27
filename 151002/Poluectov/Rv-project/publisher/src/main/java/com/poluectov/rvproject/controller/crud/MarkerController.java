package com.poluectov.rvproject.controller.crud;

import com.poluectov.rvproject.dto.marker.MarkerRequestTo;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.service.MarkerService;
import com.poluectov.rvproject.utils.modelassembler.MarkerModelAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/markers")
public class MarkerController extends CommonRESTController<Marker, MarkerRequestTo, MarkerResponseTo> {
    public MarkerController(MarkerService service, MarkerModelAssembler assembler) {
        super(service, assembler::toModel);
    }
}
