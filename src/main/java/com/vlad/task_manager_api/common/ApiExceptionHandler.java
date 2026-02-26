package com.vlad.task_manager_api.common;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private Map<String, Object> baseBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return body;
    }

    // 404 custom
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(baseBody(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI()));
    }

    // 400 validation (DTO/entity validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(err.getField(), err.getDefaultMessage());
        }

        Map<String, Object> body = baseBody(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                req.getRequestURI()
        );

        body.put("validationErrors", validationErrors);

        return ResponseEntity.badRequest().body(body);
    }

    // 400 malformed JSON / wrong enum / wrong date
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {

        return ResponseEntity
                .badRequest()
                .body(baseBody(
                        HttpStatus.BAD_REQUEST,
                        "Malformed JSON or invalid field value",
                        req.getRequestURI()
                ));
    }

    // 409 database constraint (very important for you)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabase(DataIntegrityViolationException ex, HttpServletRequest req) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(baseBody(
                        HttpStatus.CONFLICT,
                        "Database constraint violation (possibly null user_id or FK issue)",
                        req.getRequestURI()
                ));
    }

    // 500 fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest req) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseBody(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),   // show real cause in dev
                        req.getRequestURI()
                ));
    }
}