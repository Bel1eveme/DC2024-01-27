package api.dto.request

import kotlinx.serialization.Serializable
import util.inRange

@Serializable
data class UpdateEditorDto @Throws(IllegalArgumentException::class) constructor(
    val id: Long,
    val login: String,
    val password: String,
    val firstname: String,
    val lastname: String
) {

    init {
        require(login.inRange(2, 64))
        require(password.inRange(8, 128))
        require(firstname.inRange(2, 64))
        require(lastname.inRange(2, 64))
    }

}