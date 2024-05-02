package services

import api.dto.request.CreateEditorDto
import api.dto.request.UpdateEditorDto
import api.dto.response.EditorDto

interface EditorService {

    suspend fun create(createEditorDto: CreateEditorDto): EditorDto?

    suspend fun update(editorId: Long, updateEditorDto: UpdateEditorDto): EditorDto?

    suspend fun getById(editorId: Long): EditorDto?

    suspend fun getAll(): List<EditorDto?>

    suspend fun delete(editorId: Long): Boolean

}