package edu.lysak.account.exception;

public class InsecurePasswordException extends RuntimeException {
    public InsecurePasswordException(String message) {
        super(message);
    }
}
