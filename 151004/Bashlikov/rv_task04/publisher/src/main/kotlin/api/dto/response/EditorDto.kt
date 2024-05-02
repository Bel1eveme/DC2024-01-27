package api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class EditorDto(
    val id: Long,
    val login: String,
    val password: String,
    val firstname: String,
    val lastname: String
)