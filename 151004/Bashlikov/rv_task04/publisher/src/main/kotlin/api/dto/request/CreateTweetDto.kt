package by.bashlikovvv.api.dto.request

import util.inRange
import kotlinx.serialization.Serializable
import kotlin.jvm.Throws

@Serializable
data class CreateTweetDto @Throws(IllegalArgumentException::class) constructor(
    val editorId: Long,
    val title: String,
    val content: String? = null,
    val name: String? = null
) {

    init {
        require(title.inRange(2, 64))
        if (content != null) {
            require(content.inRange(4, 2048))
        }
    }

}