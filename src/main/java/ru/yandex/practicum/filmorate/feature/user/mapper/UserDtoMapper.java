package ru.yandex.practicum.filmorate.feature.user.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import ru.yandex.practicum.filmorate.feature.user.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.feature.user.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.feature.user.dto.UserDto;
import ru.yandex.practicum.filmorate.feature.user.dto.UserPayload;

@Component
public class UserDtoMapper {

    /**
     * In.
     */
    public User toDomainCreate(CreateUserRequest dto) {
        return createCommonUser(new User(), dto);
    }

    public User toDomainUpdate(UpdateUserRequest dto) {
        User user = createCommonUser(new User(), dto);
        user.setId(dto.getId());
        return user;
    }

    /**
     * Out.
     */
    public UserDto toDto(User user) {
        return createDto(user);
    }

    public List<UserDto> toDto(List<User> users) {
        return users.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    private UserDto createDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setBirthday(user.getBirthday());
        return dto;
    }

    private <T extends UserPayload >User createCommonUser(User user, T dto) {
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        return user;
    }
}
