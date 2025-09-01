package ru.yandex.practicum.filmorate.common.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.common.config.AppProperties;
import ru.yandex.practicum.filmorate.common.web.error.ErrorResponse;
import ru.yandex.practicum.filmorate.common.web.error.Violation;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    private final AppProperties appProperties;

    public GlobalErrorHandler(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request) {
        List<Violation> violations = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                violations.add(new Violation(error.getField(), error.getDefaultMessage()))
        );

        String message = appProperties.isDev()
                ? "Validation failed: " + ex.getMessage()
                : "Validation failed.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                message,
                request.getRequestURI(),
                violations
        );

        log.warn("Validation error at {}: {}", request.getRequestURI(), violations);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        List<Violation> violations = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {

            String path = constraintViolation.getPropertyPath() != null
                    ? constraintViolation.getPropertyPath().toString() : "";

            violations.add(new Violation(path, constraintViolation.getMessage()));
        }

        String message = appProperties.isDev()
                ? "Constraint violation: " + ex.getMessage()
                : "Validation failed.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Constraint Violation",
                message,
                request.getRequestURI(),
                violations
        );

        log.warn("Constraint violation at {}: {}", request.getRequestURI(), violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String parameter = ex.getName();

        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName() : "unknown";

        String message = appProperties.isDev()
                ? "Parameter '" + parameter + "' must be " + requiredType
                : "Invalid parameter.";

        List<Violation> violations = List.of(new Violation(parameter, message));

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Type Mismatch",
                message,
                request.getRequestURI(),
                violations
        );

        log.warn("Type mismatch at {}: {}", request.getRequestURI(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex,
                                                            HttpServletRequest request) {
        String message = appProperties.isDev()
                ? "Missing parameter: " + ex.getParameterName()
                : "Invalid request.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Parameter",
                message,
                request.getRequestURI(),
                List.of(new Violation(ex.getParameterName(), "Parameter is required"))
        );

        log.warn("Missing parameter at {}: {}", request.getRequestURI(), ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex,
                                                          HttpServletRequest request) {
        String message = appProperties.isDev()
                ? "Malformed JSON: " + ex.getMostSpecificCause().getMessage()
                : "Malformed request body.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON",
                message,
                request.getRequestURI()
        );

        log.warn("Bad JSON at {}: {}", request.getRequestURI(), ex.getMostSpecificCause().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex,
                                                        HttpServletRequest request) {
        String message = appProperties.isDev() ? ex.getMessage() : "Resource not found.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                message,
                request.getRequestURI()
        );

        log.warn("Not found at {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, DuplicateKeyException.class})
    public ResponseEntity<ErrorResponse> handleConflict(Exception ex,
                                                        HttpServletRequest request) {
        String message = appProperties.isDev()
                ? "Data integrity violation: " + ex.getMessage()
                : "Conflict.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                message,
                request.getRequestURI()
        );

        log.warn("Conflict at {}: {}", request.getRequestURI(), ex.toString());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex,
                                                       HttpServletRequest request) {
        String message = appProperties.isDev() ? ex.getMessage() : "Unexpected error occurred.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                message,
                request.getRequestURI()
        );

        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.toString(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
