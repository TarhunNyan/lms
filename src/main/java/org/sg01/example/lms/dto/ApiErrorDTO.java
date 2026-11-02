package org.sg01.example.lms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class ApiErrorDTO {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Map<String, String> validationErrors;
    // только в dev
    private final String stackTrace;
}
