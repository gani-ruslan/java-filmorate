package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

/**
 * User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    @NotBlank(message = "E-mail must not be blank.")
    @Email(message = "E-mail not valid.")
    private String email;

    @NotBlank(message = "Login must not be blank.")
    @NoSpaces
    private String login;

    private String name;

    @Past(message = "Birthday must be in past.")
    private LocalDate birthday;

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.name = user.getName();
        this.birthday = user.getBirthday();
    }
}
