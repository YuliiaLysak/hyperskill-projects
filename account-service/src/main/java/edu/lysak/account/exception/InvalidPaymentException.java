package edu.lysak.account.exception;

public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
