package edu.lysak.qrcodeapi.handler;

import edu.lysak.qrcodeapi.domain.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class QRCodeExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleValidationExceptions(IllegalArgumentException exception) {
        return ResponseEntity
            .badRequest()
            .body(new Error(exception.getMessage()));
    }
}
