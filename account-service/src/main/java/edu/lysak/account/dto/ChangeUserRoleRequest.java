package edu.lysak.account.dto;

import edu.lysak.account.domain.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeUserRoleRequest {
    @NotBlank(message = "'user' should not be blank")
    private String user;
    private String role;
    private Operation operation;
}
