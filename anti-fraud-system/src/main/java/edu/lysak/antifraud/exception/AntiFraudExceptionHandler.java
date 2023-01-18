package edu.lysak.antifraud.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AntiFraudExceptionHandler {

    @ExceptionHandler(UserRoleIsAlreadyAssigned.class)
    public ResponseEntity<?> handleUserRoleIsAlreadyAssigned() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            UserRoleIsNotSupportedException.class,
            AdministratorCannotBeBlockedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<?> handleException() {
        return ResponseEntity.badRequest().build();
    }

}
