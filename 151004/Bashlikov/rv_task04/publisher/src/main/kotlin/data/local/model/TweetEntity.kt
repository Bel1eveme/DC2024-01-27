package by.bashlikovvv.data.local.model

import by.bashlikovvv.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class TweetEntity(
    val id: Long,
    val editorId: Long,
    val title: String,
    val content: String,
    @Serializable(TimeStampSerializer::class) val created: Timestamp,
    @Serializable(TimeStampSerializer::class) val modified: Timestamp
)