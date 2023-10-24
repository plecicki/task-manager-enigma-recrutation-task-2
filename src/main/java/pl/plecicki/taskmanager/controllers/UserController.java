package pl.plecicki.taskmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.plecicki.taskmanager.domain.dtos.UserDto;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.exceptions.UserDoesntExist;
import pl.plecicki.taskmanager.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/filter")
    public ResponseEntity<List<User>> findUsers(@RequestParam String name, @RequestParam String surname) throws UserDoesntExist {
        return ResponseEntity.ok(userService.findUsers(name, surname));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws UserDoesntExist {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{userId}/tasks")
    public ResponseEntity<List<Task>> getUsersTasks(@PathVariable Long userId) throws UserDoesntExist {
        return ResponseEntity.ok(userService.getUsersTasks(userId));
    }
}
