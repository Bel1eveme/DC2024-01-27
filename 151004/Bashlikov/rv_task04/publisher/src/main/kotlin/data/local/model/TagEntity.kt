package data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class TagEntity(
    val id: Long,
    val name: String
)