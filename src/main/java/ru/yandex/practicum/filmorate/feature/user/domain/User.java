package ru.yandex.practicum.filmorate.feature.user.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.name = user.getName();
        this.birthday = user.getBirthday();
    }
}
