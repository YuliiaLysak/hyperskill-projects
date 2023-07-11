package edu.lysak.account.controller;

import edu.lysak.account.domain.User;
import edu.lysak.account.dto.ChangeRoleAccessRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<UserResponse> changeUserRole(
        @AuthenticationPrincipal User currentUser,
        @RequestBody @Valid ChangeRoleAccessRequest request,
        BindingResult bindingResult
    ) {
        UserResponse response = userService.changeUserRole(
            request,
            currentUser.getEmail(),
            "/api/admin/user/role"
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/admin/user/{userEmail}")
    public ResponseEntity<UserResponse> deleteUser(
        @AuthenticationPrincipal User currentUser,
        @PathVariable("userEmail") String userEmail
    ) {
        UserResponse response = userService.deleteUser(
            userEmail,
            currentUser.getEmail(),
            "/api/admin/user"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/admin/user/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/api/admin/user/access")
    public ResponseEntity<UserResponse> changeUserAccess(
        @AuthenticationPrincipal User currentUser,
        @RequestBody ChangeRoleAccessRequest request
    ) {
        UserResponse response = userService.changeUserAccess(
            request,
            currentUser.getEmail(),
            "/api/admin/user/access"
        );
        return ResponseEntity.ok(response);
    }
}
