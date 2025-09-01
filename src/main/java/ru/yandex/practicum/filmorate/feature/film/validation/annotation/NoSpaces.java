package ru.yandex.practicum.filmorate.feature.film.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.feature.film.validation.validator.NoSpacesValidator;

@Documented
@Constraint(validatedBy = NoSpacesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpaces {
    String message() default "Login must not contain spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}