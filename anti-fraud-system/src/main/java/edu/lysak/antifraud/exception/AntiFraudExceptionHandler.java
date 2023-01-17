package edu.lysak.antifraud.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AntiFraudExceptionHandler {

    @ExceptionHandler({
            ConstraintViolationException.class,
            UserRoleIsNotSupportedException.class,
            AdministratorCannotBeBlockedException.class
    })
    public ResponseEntity<?> handleConstraintViolationException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UserRoleIsAlreadyAssigned.class)
    public ResponseEntity<?> handleUserRoleIsAlreadyAssigned() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
