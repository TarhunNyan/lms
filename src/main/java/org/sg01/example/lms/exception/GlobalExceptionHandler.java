package org.sg01.example.lms.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.ApiErrorDTO;
import org.sg01.example.lms.helper.FeatureFlagService;
import org.sg01.example.lms.helper.StacktraceHelperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final FeatureFlagService featureFlagService;
    private final StacktraceHelperService stacktraceHelperService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "validation error", req, errors, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error", req, null, ex);
    }

    private ResponseEntity<ApiErrorDTO> build(HttpStatus status, String message, HttpServletRequest req, Map<String, String> validation, Throwable ex) {
        ApiErrorDTO.ApiErrorDTOBuilder bodyBuilder = ApiErrorDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .validationErrors(validation);

        if (featureFlagService.isDev()) {
            bodyBuilder = bodyBuilder.stackTrace(stacktraceHelperService.toStackTrace(ex));
        }

        ApiErrorDTO body = bodyBuilder.build();

        return ResponseEntity
                .status(status)
                .body(body);
    }

}
