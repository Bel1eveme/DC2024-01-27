package data.mapper

import by.bashlikovvv.api.dto.request.CreateTweetDto
import by.bashlikovvv.data.local.model.TweetEntity
import util.IMapper
import java.sql.Timestamp

class CreateTweetDtoToTweetMapper(
    private val id: Long? = null,
    private val name: String? = null
) : IMapper<CreateTweetDto, TweetEntity> {
    override fun mapFromEntity(entity: CreateTweetDto): TweetEntity {
        val created = Timestamp(System.currentTimeMillis())

        return TweetEntity(
            id = id ?: 0,
            editorId = entity.editorId,
            title = entity.title,
            content = entity.content ?: "",
            created = created,
            modified = created
        )
    }

    override fun mapToEntity(domain: TweetEntity): CreateTweetDto {
        return CreateTweetDto(
            editorId = domain.editorId,
            title = domain.title,
            content = domain.content,
            name = name
        )
    }
}