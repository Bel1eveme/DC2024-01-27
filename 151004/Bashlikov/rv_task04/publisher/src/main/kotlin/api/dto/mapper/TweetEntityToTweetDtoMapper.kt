package by.bashlikovvv.api.dto.mapper

import by.bashlikovvv.api.dto.response.TweetDto
import by.bashlikovvv.data.local.model.TweetEntity
import util.IMapper

class TweetEntityToTweetDtoMapper : IMapper<TweetEntity, TweetDto> {
    override fun mapFromEntity(entity: TweetEntity): TweetDto {
        return TweetDto(
            id = entity.id,
            editorId = entity.editorId,
            title = entity.title,
            content = entity.content,
            created = entity.created,
            modified = entity.modified
        )
    }

    override fun mapToEntity(domain: TweetDto): TweetEntity {
        return TweetEntity(
            id = domain.id,
            editorId = domain.editorId,
            title = domain.title,
            content = domain.content,
            created = domain.created,
            modified = domain.modified
        )
    }
}