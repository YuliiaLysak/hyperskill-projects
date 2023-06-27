package edu.lysak.account.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
    private String name;
    private String lastname;
    private String email;
    private String password;
}
