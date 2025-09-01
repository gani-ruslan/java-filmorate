package ru.yandex.practicum.filmorate.feature.film.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.feature.film.validation.annotation.NoSpaces;

public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return login != null && !login.contains(" ");
    }
}