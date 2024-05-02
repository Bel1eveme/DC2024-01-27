package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.sticker.service.StickerService;
import org.example.publisher.impl.sticker.dto.StickerRequestTo;
import org.example.publisher.impl.sticker.dto.StickerResponseTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/stickers")
@RequiredArgsConstructor
public class StickerController {
    private final StickerService stickerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StickerResponseTo> getStickers() {
        return stickerService.getStickers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StickerResponseTo getStickerById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return stickerService.getStickerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StickerResponseTo saveSticker(@Valid @RequestBody StickerRequestTo stickerTo) throws DuplicateEntityException, EntityNotFoundException {
        return stickerService.saveSticker(stickerTo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StickerResponseTo updateNote(@Valid @RequestBody StickerRequestTo stickerTo) throws DuplicateEntityException, EntityNotFoundException {
        return stickerService.updateSticker(stickerTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable BigInteger id) throws EntityNotFoundException {
        stickerService.deleteStickerById(id);
    }

}
