package com.example.rv.impl.label.mapper;
import com.example.rv.impl.label.Label;
import com.example.rv.impl.label.dto.LabelRequestTo;
import com.example.rv.impl.label.dto.LabelResponseTo;
import com.example.rv.impl.tweet.Tweet;

import java.util.List;

public interface LabelMapper {
    LabelRequestTo labelToRequestTo(Label label);

    List<LabelRequestTo> labelToRequestTo(Iterable<Label> labels);

    Label dtoToEntity(LabelRequestTo labelRequestTo, List<Tweet> tweets);

    LabelResponseTo labelToResponseTo(Label label);

    List<LabelResponseTo> labelToResponseTo(Iterable<Label> labels);
}
