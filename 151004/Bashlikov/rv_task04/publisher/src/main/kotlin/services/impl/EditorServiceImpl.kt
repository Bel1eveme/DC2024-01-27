package services.impl

import api.dto.mapper.EditorEntityToEditorDtoMapper
import api.dto.request.CreateEditorDto
import api.dto.request.UpdateEditorDto
import api.dto.response.EditorDto
import domain.exception.ApplicationExceptions
import data.mapper.CreateEditorDtoToEditorMapper
import data.mapper.UpdateEditorDtoToEditorMapper
import domain.repository.IEditorsRepository
import services.EditorService

class EditorServiceImpl(
    private val editorRepository: IEditorsRepository
) : EditorService {

    private val mapper = EditorEntityToEditorDtoMapper()

    override suspend fun create(createEditorDto: CreateEditorDto): EditorDto {
        val editorEntity = CreateEditorDtoToEditorMapper().mapFromEntity(createEditorDto)
        val id = editorRepository.create(editorEntity)

        return mapper.mapFromEntity(editorEntity.copy(id = id))
    }

    override suspend fun update(editorId: Long, updateEditorDto: UpdateEditorDto): EditorDto {
        val editorEntity = UpdateEditorDtoToEditorMapper().mapFromEntity(updateEditorDto)
        if (!editorRepository.update(editorId, editorEntity)) {
            throw ApplicationExceptions.UpdateException("Exception during editor updating")
        }

        return mapper.mapFromEntity(editorEntity)
    }

    override suspend fun getById(editorId: Long): EditorDto? {
        val editorEntity = editorRepository.read(editorId) ?: return null

        return mapper.mapFromEntity(editorEntity)
    }

    override suspend fun getAll(): List<EditorDto?> {
        return editorRepository.readAll()
            .filterNotNull()
            .map { mapper.mapFromEntity(it) }
    }

    override suspend fun delete(editorId: Long): Boolean {
        return editorRepository.delete(editorId)
    }

}