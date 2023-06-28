package edu.lysak.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PasswordResponse {
    private String email;
    private String status;
}
