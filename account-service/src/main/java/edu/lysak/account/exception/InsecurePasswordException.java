package edu.lysak.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InsecurePasswordException extends RuntimeException {
    private String message;

    public InsecurePasswordException(String message) {
        super(message);
    }
}
