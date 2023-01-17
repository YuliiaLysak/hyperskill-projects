package edu.lysak.antifraud.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ChangeAccessRequest {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Operation should not be empty")
    private String operation;
}