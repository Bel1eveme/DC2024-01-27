package api.dto.request

import kotlinx.serialization.Serializable
import util.inRange

@Serializable
data class UpdatePostDto @Throws(IllegalArgumentException::class) constructor(
    val id: Long,
    val tweetId: Long,
    val content: String,
    val country: String
) {
    init {
        require(content.inRange(2, 2048))
    }

}