package edu.lysak.account.controller;

import edu.lysak.account.dto.ChangeUserRoleRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
        @RequestBody @Valid ChangeUserRoleRequest request,
        BindingResult bindingResult
    ) {
        return ResponseEntity.ok(userService.changeUserRole(request));
    }

    @DeleteMapping("/api/admin/user/{userEmail}")
    public ResponseEntity<UserResponse> deleteUser(
        @PathVariable("userEmail") String userEmail
    ) {
        return ResponseEntity.ok(userService.deleteUser(userEmail));
    }

    @GetMapping("/api/admin/user/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
