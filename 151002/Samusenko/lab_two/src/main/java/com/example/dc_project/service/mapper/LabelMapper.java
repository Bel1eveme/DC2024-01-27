package com.example.dc_project.service.mapper;
import com.example.dc_project.model.entity.Label;
import com.example.dc_project.model.request.LabelRequestTo;
import com.example.dc_project.model.response.LabelResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponseTo getResponse(Label label);
    List<LabelResponseTo> getListResponse(Iterable<Label> labels);
    Label getLabel(LabelRequestTo labelRequestTo);
}
