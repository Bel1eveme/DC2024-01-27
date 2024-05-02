package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val statusCode: Int,
    val message: T? = null
)