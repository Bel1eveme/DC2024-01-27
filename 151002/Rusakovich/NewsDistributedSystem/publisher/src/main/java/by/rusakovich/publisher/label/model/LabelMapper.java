package by.rusakovich.publisher.label.model;

import by.rusakovich.publisher.generics.model.IEntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper extends IEntityMapper<Long, Label, LabelRequestTO, LabelResponseTO> {
    @Override
    Label mapToEntity(LabelRequestTO request);
    @Override
    LabelResponseTO mapToResponse(Label entity);

}
