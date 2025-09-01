package ru.yandex.practicum.filmorate.feature.film.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.feature.film.validation.annotation.MinDate;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MinDateValidator implements ConstraintValidator<MinDate, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(MinDate constraintAnnotation) {
        try {
            this.minDate = LocalDate.parse(constraintAnnotation.value());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format in @MinDate: " + constraintAnnotation.value());
        }
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date != null && !date.isBefore(minDate);
    }
}
