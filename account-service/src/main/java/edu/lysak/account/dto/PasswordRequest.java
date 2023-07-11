package edu.lysak.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordRequest {
    @JsonProperty("new_password")
    private String newPassword;
}
