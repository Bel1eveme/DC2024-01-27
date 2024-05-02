package by.bashlikovvv.services.impl

import by.bashlikovvv.api.dto.mapper.TagEntityToTagDtoMapper
import by.bashlikovvv.api.dto.request.CreateTagDto
import by.bashlikovvv.api.dto.request.UpdateTagDto
import by.bashlikovvv.api.dto.response.TagDto
import data.mapper.CreateTagDtoToTagMapper
import data.mapper.UpdateTagDtoToTagMapper
import domain.exception.ApplicationExceptions
import by.bashlikovvv.domain.repository.ITagsRepository
import by.bashlikovvv.services.TagService

class TagServiceImpl(
    private val tagsRepository: ITagsRepository
) : TagService {

    private val mapper = TagEntityToTagDtoMapper()

    override suspend fun create(createTagDto: CreateTagDto): TagDto {
        val tagEntity = CreateTagDtoToTagMapper().mapFromEntity(createTagDto)
        val id = tagsRepository.create(tagEntity)

        return mapper.mapFromEntity(tagEntity.copy(id = id))
    }

    override suspend fun getAll(): List<TagDto?> {
        return tagsRepository.readAll()
            .filterNotNull()
            .map { mapper.mapFromEntity(it) }
    }

    override suspend fun getById(tagId: Long): TagDto? {
        val tagEntity = tagsRepository.read(tagId) ?: return null

        return mapper.mapFromEntity(tagEntity)
    }

    override suspend fun update(tagId: Long, updateTagDto: UpdateTagDto): TagDto {
        val tagEntity = UpdateTagDtoToTagMapper().mapFromEntity(updateTagDto)
        if (!tagsRepository.update(tagId, tagEntity)) {
            throw ApplicationExceptions.UpdateException("Exception during tag updating")
        }

        return mapper.mapFromEntity(tagEntity)
    }

    override suspend fun delete(tagId: Long): Boolean {
        return tagsRepository.delete(tagId)
    }

}