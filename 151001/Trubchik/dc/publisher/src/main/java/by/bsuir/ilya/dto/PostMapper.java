package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    Post postResponseToToPost(PostResponseTo responseTo);
    Post postRequestToToPost(PostRequestTo requestTo);

    PostRequestTo postToPostRequestTo(Post post);

    PostResponseTo postToPostResponseTo(Post post);

    List<PostResponseTo> listRequestToResponse(List<PostRequestTo> requestList);
}
