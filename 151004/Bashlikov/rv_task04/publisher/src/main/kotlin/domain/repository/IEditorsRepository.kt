package domain.repository

import data.local.model.EditorEntity

interface IEditorsRepository {

    suspend fun create(editorEntity: EditorEntity): Long

    suspend fun read(id: Long): EditorEntity?

    suspend fun readAll(): List<EditorEntity?>

    suspend fun update(id: Long, editorEntity: EditorEntity): Boolean

    suspend fun delete(id: Long): Boolean

}