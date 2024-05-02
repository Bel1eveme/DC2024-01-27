package by.bashlikovvv.api.dto.request

import util.inRange
import kotlinx.serialization.Serializable
import kotlin.jvm.Throws

@Serializable
data class CreateTagDto @Throws(IllegalArgumentException::class) constructor(
    val name: String
) {

    init {
        require(name.inRange(2, 32))
    }

}