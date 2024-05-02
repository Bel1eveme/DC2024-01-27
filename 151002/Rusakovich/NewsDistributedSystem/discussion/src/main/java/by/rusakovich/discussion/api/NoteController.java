package by.rusakovich.discussion.api;

import by.rusakovich.discussion.model.NoteIternalRequestTO;
import by.rusakovich.discussion.model.NoteRequestTO;
import by.rusakovich.discussion.model.NoteResponseTO;
import by.rusakovich.discussion.service.INoteService;
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

    private final INoteService service;

    @PostMapping("")
    public ResponseEntity<NoteResponseTO> create(
            @RequestBody @Valid NoteRequestTO to,
            @RequestHeader(value="Accept-Language", defaultValue = "ru") String acceptLanguage){
        NoteResponseTO result = service.create(
                new NoteIternalRequestTO(acceptLanguage, to.id(), to.newsId(), to.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<NoteResponseTO>> readAll(){
        List<NoteResponseTO> result = service.readAll();
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<NoteResponseTO> read(@PathVariable Long id){
        NoteResponseTO result = service.readById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    public ResponseEntity<NoteResponseTO> update(@Valid @RequestBody NoteRequestTO to, @RequestHeader(value="Accept-Language", defaultValue = "ru") String acceptLanguage){
        NoteResponseTO result = service.update(
                new NoteIternalRequestTO(acceptLanguage, to.id(), to.newsId(), to.content()));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
