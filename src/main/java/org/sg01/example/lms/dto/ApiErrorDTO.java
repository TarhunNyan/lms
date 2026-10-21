package org.sg01.example.lms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class ApiErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;
    // только в dev
    private String stackTrace;
}
