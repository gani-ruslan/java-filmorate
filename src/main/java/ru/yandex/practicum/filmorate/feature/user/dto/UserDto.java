package ru.yandex.practicum.filmorate.feature.user.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
}
