package edu.lysak.antifraud.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ChangeRoleRequest {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Role should not be empty")
    private String role;
}