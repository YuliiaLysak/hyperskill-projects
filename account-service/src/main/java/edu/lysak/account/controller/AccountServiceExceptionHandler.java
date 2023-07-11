package edu.lysak.account.controller;

import edu.lysak.account.exception.InvalidRequestException;
import edu.lysak.account.exception.UserNotFoundException;
import edu.lysak.account.exception.UserRoleNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Set;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    @ExceptionHandler({
        InvalidRequestException.class
    })
    public void handleBadRequestException(
        RuntimeException exception,
        HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({
        UserNotFoundException.class,
        UserRoleNotFoundException.class
    })
    public void handleNotFoundException(
        RuntimeException exception,
        HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleValidationError(
        ConstraintViolationException exception,
        HttpServletResponse response
    ) throws IOException {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String errorMessage = "Error";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> {
                builder.append(violation.getMessageTemplate());
                builder.append("; ");
            });
            builder.delete(builder.length() - 2, builder.length());
            errorMessage = builder.toString();
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
    }

}
