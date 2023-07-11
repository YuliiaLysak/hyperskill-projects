package edu.lysak.account.controller;

import edu.lysak.account.domain.User;
import edu.lysak.account.dto.PasswordRequest;
import edu.lysak.account.dto.PasswordResponse;
import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<UserResponse> signup(
        @RequestBody @Valid UserRequest userRequest,
        BindingResult bindingResult
    ) {
        return ResponseEntity.ok(userService.signupUser(userRequest, "/api/auth/signup"));
    }

    @PostMapping("/api/auth/changepass")
    public ResponseEntity<PasswordResponse> changePassword(
        @AuthenticationPrincipal User user,
        @RequestBody PasswordRequest passwordRequest
    ) {
        PasswordResponse response = userService.changePassword(user, passwordRequest.getNewPassword(), "/api/auth/changepass");
        return ResponseEntity.ok(response);
    }
}
