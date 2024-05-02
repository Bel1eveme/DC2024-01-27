package data.mapper

import api.dto.request.UpdatePostDto
import data.local.model.PostEntity
import util.IMapper

class UpdatePostDtoToPostMapper : IMapper<UpdatePostDto, PostEntity> {
    override fun mapFromEntity(entity: UpdatePostDto): PostEntity {
        return PostEntity(
            id = entity.id,
            tweetId = entity.tweetId,
            content = entity.content,
            country = entity.country
        )
    }

    override fun mapToEntity(domain: PostEntity): UpdatePostDto {
        return UpdatePostDto(
            id = domain.id,
            tweetId = domain.tweetId,
            content = domain.content,
            country = domain.country
        )
    }
}