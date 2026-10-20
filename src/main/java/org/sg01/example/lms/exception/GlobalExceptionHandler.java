package org.sg01.example.lms.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sg01.example.lms.helper.FeatureFlagService;
import org.sg01.example.lms.helper.StacktraceHelperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final FeatureFlagService featureFlagService;
    private final StacktraceHelperService stacktraceHelperService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), null, ex);
    }

    @ExceptionHandler(StudentAlreadyInGroupException.class)
    public ProblemDetail handleStudentAlreadyInGroup(StudentAlreadyInGroupException ex) {
        return problem(HttpStatus.CONFLICT, "Cannot create relationship, already exist", ex.getMessage(), null, ex);
    }

    @ExceptionHandler(LastGroupRemovalException.class)
    public ProblemDetail handleLastGroupRemoval(LastGroupRemovalException ex) {
        return problem(HttpStatus.CONFLICT, "Cannot remove student from the last group", ex.getMessage(), null, ex);
    }

    @ExceptionHandler(GroupHasStudentsException.class)
    public ProblemDetail handleGroupHasStudents(GroupHasStudentsException ex) {
        return problem(HttpStatus.CONFLICT, "Cannot delete group", ex.getMessage(), null, ex);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ProblemDetail handleInvalidDateRange(InvalidDateRangeException ex) {
        log.warn("Invalid date range: {}", ex.getMessage());
        return problem(HttpStatus.BAD_REQUEST, "Invalid date range", ex.getMessage(), null, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex
                .getBindingResult()
                .getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return problem(HttpStatus.BAD_REQUEST, "Validation error", "Request validation failed", errors, ex);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception on {} {}", req.getMethod(), req.getRequestURI(), ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Unexpected error occurred", null, ex);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, Map<String, String> errors, Throwable ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", Instant.now());
        if (errors != null && !errors.isEmpty()) {
            pd.setProperty("errors", errors);
        }
        if (featureFlagService.isDev() && ex != null) {
            pd.setProperty("stackTrace", stacktraceHelperService.toStackTrace(ex));
        }
        return pd;
    }
}
