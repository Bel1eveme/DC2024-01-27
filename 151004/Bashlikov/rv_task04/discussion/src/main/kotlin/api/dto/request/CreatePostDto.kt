package api.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import util.inRange

@Serializable
data class CreatePostDto @Throws(IllegalArgumentException::class) constructor(
    @SerialName("id") val id: Long? = 0,
    @SerialName("tweetId") val tweetId: Long,
    @SerialName("content") val content: String
) {
    init {
        require(content.inRange(2, 2048))
    }

}