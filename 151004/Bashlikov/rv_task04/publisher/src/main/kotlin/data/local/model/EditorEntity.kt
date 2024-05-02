package data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class EditorEntity(
    val id: Long,
    val login: String,
    val password: String,
    val firstname: String,
    val lastname: String
)