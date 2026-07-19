package com.example.crdpractice.common;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiError> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        String correlationId = correlationId(request);
        ApiError error = new ApiError(
                Instant.now(),
                exception.getStatus().value(),
                exception.getErrorCode(),
                exception.getMessage(),
                List.of(),
                correlationId);
        return ResponseEntity.status(exception.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();
        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed.",
                fieldErrors,
                correlationId(request));
        return ResponseEntity.badRequest().body(apiError);
    }

    private String correlationId(HttpServletRequest request) {
        String header = request.getHeader("X-Correlation-Id");
        return header == null || header.isBlank() ? "missing" : header;
    }
}
