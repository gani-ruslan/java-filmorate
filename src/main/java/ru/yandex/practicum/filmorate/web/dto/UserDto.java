package ru.yandex.practicum.filmorate.web.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Email must not be empty.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Login must not be empty.")
    @Size(max = 50, message = "Login must be 50 chars max.")
    private String login;

    @Size(max = 100, message = "Name must be 100 chars max.")
    private String name;

    @Past(message = "Birthday must be in the past.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
