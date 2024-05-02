package com.example.rv.impl.tag.mapper;
import com.example.rv.impl.tag.Tag;
import com.example.rv.impl.tag.dto.TagRequestTo;
import com.example.rv.impl.tag.dto.TagResponseTo;
import com.example.rv.impl.tweet.Tweet;

import java.util.List;

public interface TagMapper {
    TagRequestTo tagToRequestTo(Tag tag);

    List<TagRequestTo> tagToRequestTo(Iterable<Tag> tags);

    Tag dtoToEntity(TagRequestTo tagRequestTo, List<Tweet> tweets);

    TagResponseTo tagToResponseTo(Tag tag);

    List<TagResponseTo> tagToResponseTo(Iterable<Tag> tags);
}
