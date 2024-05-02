package by.bashlikovvv.api.dto.request

import util.inRange
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTweetDto @Throws(IllegalArgumentException::class) constructor(
    val id: Long? = null,
    val editorId: Long,
    val title: String,
    val content: String,
    val name: String? = null
) {

    init {
        require(title.inRange(2, 64))
        require(content.inRange(4, 2048))
    }

}