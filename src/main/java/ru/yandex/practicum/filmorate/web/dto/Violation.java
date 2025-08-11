package ru.yandex.practicum.filmorate.web.dto;

import lombok.Getter;

@Getter
public class Violation {
    private final String field;
    private final String message;

    public Violation(String field, String message) {
        this.field = field;
        this.message = message;
    }
}