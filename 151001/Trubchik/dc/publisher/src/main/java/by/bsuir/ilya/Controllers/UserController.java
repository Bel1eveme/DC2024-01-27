package by.bsuir.ilya.Controllers;

import by.bsuir.ilya.Service.UserService;
import by.bsuir.ilya.dto.UserRequestTo;
import by.bsuir.ilya.dto.UserResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseTo>> getAllUsers() {
        List<UserResponseTo> userResponseToList = userService.getAll();
        return new ResponseEntity<>(userResponseToList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseTo> getUser(@PathVariable long id) {
        UserResponseTo userResponseTo = userService.getById(id);
        return new ResponseEntity<>(userResponseTo, userResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseTo> createUser(@RequestBody UserRequestTo userTo) {
        return  userService.add(userTo);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseTo> deleteUser(@PathVariable long id) {
        return new ResponseEntity<>(null,userService.deleteById(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users")
    public ResponseEntity<UserResponseTo> updateUser(@RequestBody UserRequestTo userRequestTo) {
        UserResponseTo userResponseTo = userService.update(userRequestTo);
        return new ResponseEntity<>(userResponseTo, userResponseTo.getFirstname() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

}
