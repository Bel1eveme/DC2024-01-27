package data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class PostEntity(
    val id: Long,
    val country: String,
    val tweetId: Long,
    val content: String
)