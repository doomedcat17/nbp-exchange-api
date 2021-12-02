package com.doomedcat17.nbpexchangeapi.exceptions;

import com.doomedcat17.nbpexchangeapi.exceptions.CurrencyNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> invalidCurrencyCode(Exception exception) {
        ErrorResponse error = new ErrorResponse();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setStatus(httpStatus.value());
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> invalidDate() {
        ErrorResponse error = new ErrorResponse();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        error.setTimestamp(LocalDateTime.now());
        error.setMessage("Invalid date format");
        error.setStatus(httpStatus.value());
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(MissingRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingParameter(Exception e) {
        ErrorResponse error = new ErrorResponse();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(e.getMessage());
        error.setStatus(httpStatus.value());
        return new ResponseEntity<>(error, httpStatus);
    }


    static class ErrorResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime timestamp;
        private int status;
        private String message;

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
