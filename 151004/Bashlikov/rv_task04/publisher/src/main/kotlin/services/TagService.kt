package by.bashlikovvv.services

import by.bashlikovvv.api.dto.request.CreateTagDto
import by.bashlikovvv.api.dto.request.UpdateTagDto
import by.bashlikovvv.api.dto.response.TagDto

interface TagService {

    suspend fun create(createTagDto: CreateTagDto): TagDto?

    suspend fun getAll(): List<TagDto?>

    suspend fun getById(tagId: Long): TagDto?

    suspend fun update(tagId: Long, updateTagDto: UpdateTagDto): TagDto?

    suspend fun delete(tagId: Long): Boolean

}