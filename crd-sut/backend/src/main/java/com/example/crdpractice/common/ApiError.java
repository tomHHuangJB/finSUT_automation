package com.example.crdpractice.common;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String errorCode,
        String message,
        List<FieldErrorResponse> fieldErrors,
        String correlationId) {
}
