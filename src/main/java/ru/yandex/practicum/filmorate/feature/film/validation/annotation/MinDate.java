package ru.yandex.practicum.filmorate.feature.film.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.feature.film.validation.validator.MinDateValidator;

@Documented
@Constraint(validatedBy = MinDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinDate {
    String value();
    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
