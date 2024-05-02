package data.mapper

import api.dto.request.UpdateEditorDto
import data.local.model.EditorEntity
import util.IMapper

class UpdateEditorDtoToEditorMapper : IMapper<UpdateEditorDto, EditorEntity> {
    override fun mapFromEntity(entity: UpdateEditorDto): EditorEntity {
        return EditorEntity(
            id = entity.id,
            login = entity.login,
            password = entity.password,
            firstname = entity.firstname,
            lastname = entity.lastname
        )
    }

    override fun mapToEntity(domain: EditorEntity): UpdateEditorDto {
        return UpdateEditorDto(
            id = domain.id,
            login = domain.login,
            password = domain.password,
            firstname = domain.firstname,
            lastname = domain.lastname
        )
    }
}