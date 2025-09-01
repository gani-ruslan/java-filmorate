package ru.yandex.practicum.filmorate.feature.user.dto;

import java.time.LocalDate;

public interface UserPayload {
    String getName();

    String getEmail();

    String getLogin();

    LocalDate getBirthday();
}
