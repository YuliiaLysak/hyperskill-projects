package edu.lysak.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import edu.lysak.recipes.model.User;
import edu.lysak.recipes.service.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public void registerNewUser(@RequestBody @Valid User user) {
        userService.registerNewUser(user);
        throw new ResponseStatusException(HttpStatus.OK);
    }
}
