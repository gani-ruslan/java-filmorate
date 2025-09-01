package ru.yandex.practicum.filmorate.feature.user.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.feature.film.validation.annotation.NoSpaces;

@Data
public class CreateUserRequest implements UserPayload {
    @Size(max = 100, message = "Name must be 100 chars max.")
    private String name;

    @NotBlank(message = "Email must not be empty.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Login must not be empty.")
    @Size(max = 50, message = "Login must be 50 chars max.")
    @NoSpaces
    private String login;

    @Past(message = "Birthday must be in the past.")
    private LocalDate birthday;
}
