package api.dto.mapper

import api.dto.response.EditorDto
import data.local.model.EditorEntity
import util.IMapper

class EditorEntityToEditorDtoMapper : IMapper<EditorEntity, EditorDto> {
    override fun mapFromEntity(entity: EditorEntity): EditorDto {
        return EditorDto(
            id = entity.id,
            login = entity.login,
            password = entity.password,
            firstname = entity.firstname,
            lastname = entity.lastname
        )
    }

    override fun mapToEntity(domain: EditorDto): EditorEntity {
        return EditorEntity(
            id = domain.id,
            login = domain.login,
            password = domain.password,
            firstname = domain.firstname,
            lastname = domain.lastname
        )
    }
}