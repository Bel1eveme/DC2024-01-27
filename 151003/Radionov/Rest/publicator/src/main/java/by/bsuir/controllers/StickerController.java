package by.bsuir.controllers;

import by.bsuir.dto.PostResponseTo;
import by.bsuir.dto.StickerRequestTo;
import by.bsuir.dto.StickerResponseTo;
import by.bsuir.services.StickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/stickers")
public class StickerController {
    @Autowired
    StickerService stickerService;

    @GetMapping
    public ResponseEntity<List<StickerResponseTo>> getStickers(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ResponseEntity.status(200).body(stickerService.getStickers(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StickerResponseTo> getSticker(@PathVariable Long id) {
        return ResponseEntity.status(200).body(stickerService.getStickerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSticker(@PathVariable Long id) {
        stickerService.deleteSticker(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<StickerResponseTo> saveSticker(@RequestBody StickerRequestTo sticker) {
        StickerResponseTo savedSticker = stickerService.saveSticker(sticker);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSticker);
    }

    @PutMapping()
    public ResponseEntity<StickerResponseTo> updateSticker(@RequestBody StickerRequestTo sticker) {
        return ResponseEntity.status(HttpStatus.OK).body(stickerService.updateSticker(sticker));
    }

    @GetMapping("/byTweet/{id}")
    public ResponseEntity<List<StickerResponseTo>> getAuthorByTweetId(@PathVariable Long id){
        return ResponseEntity.status(200).body(stickerService.getStickerByTweetId(id));
    }
}