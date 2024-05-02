package data.repository

import data.local.dao.PostOfflineSource
import data.local.model.PostEntity
import domain.repository.IPostsRepository

class PostsRepository(
    private val postOfflineSource: PostOfflineSource
) : IPostsRepository {
    override suspend fun create(postEntity: PostEntity): Long {
        return postOfflineSource.create(postEntity)
    }

    override suspend fun read(id: Long): PostEntity? {
        return try {
            postOfflineSource.read(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun readAll(): List<PostEntity?> {
        return postOfflineSource.readAll()
    }

    override suspend fun update(id: Long, postEntity: PostEntity): Boolean {
        return postOfflineSource.update(id, postEntity) > 0
    }

    override suspend fun delete(id: Long): Boolean {
        return postOfflineSource.delete(id) > 0
    }
}