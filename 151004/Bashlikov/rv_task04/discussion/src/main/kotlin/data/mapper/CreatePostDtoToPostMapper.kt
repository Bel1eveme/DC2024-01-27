package data.mapper

import api.dto.request.CreatePostDto
import data.local.model.PostEntity
import util.IMapper

class CreatePostDtoToPostMapper(
    private val id: Long? = null
) : IMapper<CreatePostDto, PostEntity> {
    override fun mapFromEntity(entity: CreatePostDto): PostEntity {
        return PostEntity(
            id = id ?: 0,
            tweetId = entity.tweetId,
            content = entity.content,
            country = "test"
        )
    }

    override fun mapToEntity(domain: PostEntity): CreatePostDto {
        return CreatePostDto(
            id = domain.id,
            tweetId = domain.tweetId,
            content = domain.content
        )
    }
}