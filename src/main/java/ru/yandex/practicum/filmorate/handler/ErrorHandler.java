package ru.yandex.practicum.filmorate.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.config.AppMode;
import ru.yandex.practicum.filmorate.dto.ErrorResponse;
import ru.yandex.practicum.filmorate.dto.Violation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final AppMode appMode;

    public ErrorHandler(AppMode appMode) {
        this.appMode = appMode;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<Violation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(),
                        error.getDefaultMessage()))
                .toList();

        String message = appMode.isDev()
                ? "Validation failed: " + ex.getMessage()
                : "Invalid request.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                message,
                request.getRequestURI(),
                violations
        );

        log.warn("Validation error at {}: {}",
                request.getRequestURI(), violations);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex,
                                                        HttpServletRequest request) {
        String message = appMode.isDev()
                ? ex.getMessage()
                : "Unexpected error occurred.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                message,
                request.getRequestURI()
        );

        log.warn("Not found at {}: {}",
                request.getRequestURI(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex,
                                                       HttpServletRequest request) {

        String message = appMode.isDev()
                ? ex.getMessage()
                : "Unexpected error occurred.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                message,
                request.getRequestURI()
        );

        log.error("Unexpected error at {}: {}",
                request.getRequestURI(),
                ex.toString(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
