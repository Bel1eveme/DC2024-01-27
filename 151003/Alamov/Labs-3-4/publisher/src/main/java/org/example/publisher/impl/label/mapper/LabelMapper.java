package org.example.publisher.impl.label.mapper;
import org.example.publisher.impl.label.Label;
import org.example.publisher.impl.label.dto.LabelRequestTo;
import org.example.publisher.impl.label.dto.LabelResponseTo;
import org.example.publisher.impl.tweet.Tweet;

import java.util.List;

public interface LabelMapper {
    LabelRequestTo labelToRequestTo(Label label);

    List<LabelRequestTo> labelToRequestTo(Iterable<Label> labels);

    Label dtoToEntity(LabelRequestTo labelRequestTo, List<Tweet> tweets);

    LabelResponseTo labelToResponseTo(Label label);

    List<LabelResponseTo> labelToResponseTo(Iterable<Label> labels);
}
