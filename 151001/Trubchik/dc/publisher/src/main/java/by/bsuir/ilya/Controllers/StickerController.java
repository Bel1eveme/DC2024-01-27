package by.bsuir.ilya.Controllers;

import by.bsuir.ilya.Service.StickerService;
import by.bsuir.ilya.dto.StickerRequestTo;
import by.bsuir.ilya.dto.StickerResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class StickerController {
    @Autowired
    private StickerService stickerService;

    @GetMapping("/stickers")
    public ResponseEntity<List<StickerResponseTo>> getAllStickers() {
        List<StickerResponseTo> StickerResponseToList = stickerService.getAll();
        return new ResponseEntity<>(StickerResponseToList, HttpStatus.OK);
    }

    @GetMapping("/stickers/{id}")
    public ResponseEntity<StickerResponseTo> getSticker(@PathVariable long id) {
        StickerResponseTo StickerResponseTo = stickerService.getById(id);
        return new ResponseEntity<>(StickerResponseTo, StickerResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/stickers")
    public ResponseEntity<StickerResponseTo> createSticker(@RequestBody StickerRequestTo StickerTo) {
        return stickerService.add(StickerTo);
    }

    @DeleteMapping("/stickers/{id}")
    public ResponseEntity<StickerResponseTo> deleteSticker(@PathVariable long id) {
        return new ResponseEntity<>(null, stickerService.deleteById(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/stickers")
    public ResponseEntity<StickerResponseTo> updateSticker(@RequestBody StickerRequestTo StickerRequestTo) {
        StickerResponseTo StickerResponseTo = stickerService.update(StickerRequestTo);
        return new ResponseEntity<>(StickerResponseTo, StickerResponseTo.getName() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
