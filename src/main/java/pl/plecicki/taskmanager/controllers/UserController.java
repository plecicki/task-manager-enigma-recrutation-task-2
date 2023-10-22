package pl.plecicki.taskmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.plecicki.taskmanager.domain.dtos.UserDto;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.exceptions.UserDoesntExist;
import pl.plecicki.taskmanager.services.UserService;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/filter")
    public List<User> findUsers(@RequestParam String name, @RequestParam String surname) throws UserDoesntExist {
        return userService.findUsers(name, surname);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }
}
