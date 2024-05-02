package by.bashlikovvv.api.dto.mapper

import by.bashlikovvv.api.dto.response.TagDto
import data.local.model.TagEntity
import util.IMapper

class TagEntityToTagDtoMapper : IMapper<TagEntity, TagDto> {
    override fun mapFromEntity(entity: TagEntity): TagDto {
        return TagDto(
            id = entity.id,
            name = entity.name
        )
    }

    override fun mapToEntity(domain: TagDto): TagEntity {
        return TagEntity(
            id = domain.id,
            name = domain.name
        )
    }
}