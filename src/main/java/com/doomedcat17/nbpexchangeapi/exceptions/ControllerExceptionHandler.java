package com.doomedcat17.nbpexchangeapi.exceptions;

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
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), exception.getMessage());
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> invalidDate() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), "Invalid date format");
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(MissingRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingParameter(Exception e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), e.getMessage());
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound() {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), "Not found");
        error.setDate(LocalDateTime.now());
        return new ResponseEntity<>(error, httpStatus);
    }


    static class ErrorResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime date;
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
            this.date = LocalDateTime.now();
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
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
