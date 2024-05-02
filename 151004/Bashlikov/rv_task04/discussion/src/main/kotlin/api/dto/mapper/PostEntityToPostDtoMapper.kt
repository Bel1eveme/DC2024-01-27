package api.dto.mapper

import api.dto.response.PostDto
import data.local.model.PostEntity
import util.IMapper

class PostEntityToPostDtoMapper : IMapper<PostEntity, PostDto> {
    override fun mapFromEntity(entity: PostEntity): PostDto {
        return PostDto(
            id = entity.id,
            tweetId = entity.tweetId,
            content = entity.content,
            country = entity.country
        )
    }

    override fun mapToEntity(domain: PostDto): PostEntity {
        return PostEntity(
            id = domain.id,
            tweetId = domain.tweetId,
            content = domain.content,
            country = domain.country
        )
    }
}