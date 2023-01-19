package edu.lysak.antifraud.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AntiFraudExceptionHandler {

    @ExceptionHandler({
            UserRoleIsAlreadyAssigned.class,
            TransactionFeedbackIsAlreadyAssignedException.class
    })
    public ResponseEntity<?> handleConflictException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            UserRoleIsNotSupportedException.class,
            AdministratorCannotBeBlockedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<?> handleBadRequestException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({
            UnprocessableTransactionFeedback.class
    })
    public ResponseEntity<?> handleUnprocessableException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

}
