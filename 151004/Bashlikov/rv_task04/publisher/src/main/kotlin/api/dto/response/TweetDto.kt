package by.bashlikovvv.api.dto.response

import by.bashlikovvv.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class TweetDto(
    val id: Long,
    val editorId: Long,
    val title: String,
    val content: String,
    @Serializable(TimeStampSerializer::class) val created: Timestamp,
    @Serializable(TimeStampSerializer::class) val modified: Timestamp
)