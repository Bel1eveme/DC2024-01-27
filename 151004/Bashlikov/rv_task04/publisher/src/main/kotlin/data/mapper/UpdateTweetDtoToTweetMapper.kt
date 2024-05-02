package data.mapper

import by.bashlikovvv.api.dto.request.UpdateTweetDto
import by.bashlikovvv.data.local.model.TweetEntity
import util.IMapper

class UpdateTweetDtoToTweetMapper(
    private val tweet: TweetEntity
) : IMapper<UpdateTweetDto, TweetEntity> {
    override fun mapFromEntity(entity: UpdateTweetDto): TweetEntity {
        return TweetEntity(
            id = tweet.id,
            editorId = entity.editorId,
            title = entity.title,
            content = entity.content,
            created = tweet.created,
            modified = tweet.modified
        )
    }

    override fun mapToEntity(domain: TweetEntity): UpdateTweetDto {
        TODO("Not yet implemented")
    }
}