package edu.lysak.antifraud.controller;

import edu.lysak.antifraud.domain.user.ChangeAccessRequest;
import edu.lysak.antifraud.domain.user.ChangeAccessResponse;
import edu.lysak.antifraud.domain.user.ChangeRoleRequest;
import edu.lysak.antifraud.domain.user.DeletedUserResponse;
import edu.lysak.antifraud.domain.user.UserRequest;
import edu.lysak.antifraud.domain.user.UserResponse;
import edu.lysak.antifraud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponse> addUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.addNewUser(userRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersAscending());
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<DeletedUserResponse> deleteUser(@PathVariable("username") String username) {
        boolean isDeleted = userService.deleteUser(username);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DeletedUserResponse(username, "Deleted successfully!"));
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponse> changeRole(@RequestBody @Valid ChangeRoleRequest request) {
        return userService.changeUserRole(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<ChangeAccessResponse> changeAccess(@RequestBody @Valid ChangeAccessRequest request) {
        return userService.changeAccess(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
