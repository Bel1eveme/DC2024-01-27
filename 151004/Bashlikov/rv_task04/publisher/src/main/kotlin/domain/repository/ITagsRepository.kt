package by.bashlikovvv.domain.repository

import data.local.model.TagEntity

interface ITagsRepository {

    suspend fun create(tagEntity: TagEntity): Long

    suspend fun read(id: Long): TagEntity?

    suspend fun readAll(): List<TagEntity?>

    suspend fun update(id: Long, tagEntity: TagEntity): Boolean

    suspend fun delete(id: Long): Boolean

}