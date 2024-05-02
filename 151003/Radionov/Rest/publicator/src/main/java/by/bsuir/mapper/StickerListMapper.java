package by.bsuir.mapper;

import by.bsuir.dto.StickerRequestTo;
import by.bsuir.dto.StickerResponseTo;
import by.bsuir.entities.Sticker;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = StickerMapper.class)
public interface StickerListMapper {
    List<Sticker> toStickerList(List<StickerRequestTo> stickers);
    List<StickerResponseTo> toStickerResponseList(List<Sticker> stickers);
}
