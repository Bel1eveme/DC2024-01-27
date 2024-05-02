package org.example.publisher.impl.sticker.mapper.Impl;

import org.example.publisher.impl.sticker.Sticker;
import org.example.publisher.impl.sticker.mapper.StickerMapper;
import org.example.publisher.impl.sticker.dto.StickerRequestTo;
import org.example.publisher.impl.sticker.dto.StickerResponseTo;
import org.example.publisher.impl.news.News;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StickerMapperImpl implements StickerMapper {
    @Override
    public StickerRequestTo stickerToRequestTo(Sticker sticker) {
        List<BigInteger> ids = new ArrayList<>();
        for (var item: sticker.getNews()) {
            ids.add(item.getId());
        }

        return new StickerRequestTo(
                sticker.getTg_id(),
                sticker.getName(),
                ids
        );
    }

    @Override
    public List<StickerRequestTo> stickerToRequestTo(Iterable<Sticker> stickers) {
        return StreamSupport.stream(stickers.spliterator(), false)
                .map(this::stickerToRequestTo)
                .collect(Collectors.toList());
    }

    @Override
    public Sticker dtoToEntity(StickerRequestTo stickerRequestTo, List<News> twets) {
        return new Sticker(
                stickerRequestTo.getId(),
                stickerRequestTo.getName(),
                twets
        );
    }

    @Override
    public StickerResponseTo stickerToResponseTo(Sticker sticker) {
        List<BigInteger> ids = new ArrayList<>();
        for (var item: sticker.getNews()) {
            ids.add(item.getId());
        }

        return new StickerResponseTo(
                sticker.getTg_id(),
                sticker.getName(),
                ids
        );
    }

    @Override
    public List<StickerResponseTo> stickerToResponseTo(Iterable<Sticker> stickers) {
        return StreamSupport.stream(stickers.spliterator(), false)
                .map(this::stickerToResponseTo)
                .collect(Collectors.toList());
    }
}
