package ru.yandex.practicum.filmorate.web.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.web.dto.UserDto;

@Component
public class UserDtoMapper {

    public User toDomain(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setLogin(dto.getLogin());
        user.setName(dto.getName());
        user.setBirthday(dto.getBirthday());
        user.setFriends(dto.getFriends());
        return user;
    }

    public UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setLogin(u.getLogin());
        dto.setName(u.getName());
        dto.setBirthday(u.getBirthday());
        dto.setFriends(u.getFriends());
        return dto;
    }
}
