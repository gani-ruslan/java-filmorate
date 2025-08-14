package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.web.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.MpaRating;

public final class MpaRatingDtoMapper {
    private MpaRatingDtoMapper() {
    }

    public static MpaRatingDto toDto(MpaRating rating) {
        return new MpaRatingDto(rating.getId(), rating.getName());
    }
}
