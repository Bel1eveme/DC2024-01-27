package domain.repository

import data.local.model.PostEntity

interface IPostsRepository {

    suspend fun create(postEntity: PostEntity): Long

    suspend fun read(id: Long): PostEntity?

    suspend fun readAll(): List<PostEntity?>

    suspend fun update(id: Long, postEntity: PostEntity): Boolean

    suspend fun delete(id: Long): Boolean

}