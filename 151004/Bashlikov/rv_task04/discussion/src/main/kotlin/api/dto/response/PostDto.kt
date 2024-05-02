package api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: Long,
    val tweetId: Long,
    val content: String,
    val country: String
)