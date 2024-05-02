package by.bsuir.romankokarev.controller;

import by.bsuir.romankokarev.service.UserService;
import by.bsuir.romankokarev.dto.UserRequestTo;
import by.bsuir.romankokarev.dto.UserResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value = "users")
    public ResponseEntity<?> create(@RequestBody UserRequestTo user) {
        UserResponseTo u = userService.create(user);

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @GetMapping(value = "users", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<UserResponseTo>> read(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction) {
        final List<UserResponseTo> users = userService.readAll(pageInd, numOfElem, sortedBy, direction);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}")
    public ResponseEntity<UserResponseTo> read(@PathVariable(name = "id") int id) {
        final UserResponseTo user = userService.read(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "users")
    public ResponseEntity<?> update(@RequestBody UserRequestTo user) {
        final UserResponseTo updated = userService.update(user, user.getId());

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(value = "users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        userService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
