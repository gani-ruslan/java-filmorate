package ru.yandex.practicum.filmorate.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<Violation> violations;

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.violations = null;
    }

    public ErrorResponse(int status, String error, String message, String path,
                         List<Violation> violations) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.violations = violations;
    }
}