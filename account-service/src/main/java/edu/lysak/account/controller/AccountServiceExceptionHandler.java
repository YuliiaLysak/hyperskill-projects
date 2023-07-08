package edu.lysak.account.controller;

import edu.lysak.account.exception.InsecurePasswordException;
import edu.lysak.account.exception.InvalidPaymentException;
import edu.lysak.account.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Set;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    @ExceptionHandler({
        UserAlreadyExistsException.class,
        InsecurePasswordException.class,
        InvalidPaymentException.class
    })
    public void handleBadRequestException(
        RuntimeException exception,
        HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationError(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String errorMessage = "Error";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" ").append(violation.getMessage()));
            errorMessage = builder.toString();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
