package by.bashlikovvv.api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Long,
    val name: String
)