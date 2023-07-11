package edu.lysak.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    @NotBlank(message = "'name' should not be blank")
    private String name;

    @NotBlank(message = "'lastname' should not be blank")
    private String lastname;

    @NotBlank(message = "'email' should not be blank")
    @Email(message = "'email' should ends with @acme.com", regexp = ".+@acme\\.com$")
    private String email;

    @NotBlank(message = "'password' should not be blank")
    private String password;
}
