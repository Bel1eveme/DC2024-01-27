package data.repository

import data.local.dao.TagOfflineSource
import data.local.model.TagEntity
import by.bashlikovvv.domain.repository.ITagsRepository

class TagsRepository(
    private val tagOfflineSource: TagOfflineSource
) : ITagsRepository {
    override suspend fun create(tagEntity: TagEntity): Long {
        return tagOfflineSource.create(tagEntity)
    }

    override suspend fun read(id: Long): TagEntity? {
        return try {
            tagOfflineSource.read(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun readAll(): List<TagEntity?> {
        return tagOfflineSource.readAll()
    }

    override suspend fun update(id: Long, tagEntity: TagEntity): Boolean {
        return tagOfflineSource.update(id, tagEntity) > 0
    }

    override suspend fun delete(id: Long): Boolean {
        return tagOfflineSource.delete(id) > 0
    }
}