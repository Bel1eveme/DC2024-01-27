package by.rusakovich.publisher.note.api;

import by.rusakovich.publisher.note.NoteService;
import by.rusakovich.publisher.note.model.NoteKafkaRequestTO;
import by.rusakovich.publisher.note.model.NoteRequestTO;
import by.rusakovich.publisher.note.model.NoteResponseTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.url}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("")
    public ResponseEntity<NoteResponseTO> create(
            @RequestBody @Valid NoteRequestTO to,
            @RequestHeader(value="Accept-Language", defaultValue = "ru") String acceptLanguage){
        NoteResponseTO result = noteService.create(
                new NoteKafkaRequestTO(acceptLanguage, to.id(), to.newsId(), to.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<NoteResponseTO>> readAll(){
        List<NoteResponseTO> result = noteService.readAll();
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<NoteResponseTO> read(@PathVariable Long id){
        NoteResponseTO result = noteService.readById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    public ResponseEntity<NoteResponseTO> update(@Valid @RequestBody NoteRequestTO to, @RequestHeader(value="Accept-Language", defaultValue = "ru") String acceptLanguage){
        NoteResponseTO result = noteService.update(
                new NoteKafkaRequestTO(acceptLanguage, to.id(), to.newsId(), to.content()));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        noteService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
