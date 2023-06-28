package edu.lysak.account.controller;

import edu.lysak.account.domain.User;
import edu.lysak.account.dto.PasswordRequest;
import edu.lysak.account.dto.PasswordResponse;
import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        return userService.signupUser(userRequest)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/api/auth/changepass")
    public ResponseEntity<PasswordResponse> changePassword(
        @AuthenticationPrincipal User user,
        @RequestBody PasswordRequest passwordRequest
    ) {
        return ResponseEntity.ok(
            userService.changePassword(user, passwordRequest.getNewPassword())
        );
    }
}
