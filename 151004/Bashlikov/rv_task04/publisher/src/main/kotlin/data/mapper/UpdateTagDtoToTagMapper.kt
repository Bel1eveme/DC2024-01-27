package data.mapper

import by.bashlikovvv.api.dto.request.UpdateTagDto
import data.local.model.TagEntity
import util.IMapper

class UpdateTagDtoToTagMapper : IMapper<UpdateTagDto, TagEntity> {
    override fun mapFromEntity(entity: UpdateTagDto): TagEntity {
        return TagEntity(
            id = entity.id,
            name = entity.name
        )
    }

    override fun mapToEntity(domain: TagEntity): UpdateTagDto {
        return UpdateTagDto(
            id = domain.id,
            name = domain.name
        )
    }
}