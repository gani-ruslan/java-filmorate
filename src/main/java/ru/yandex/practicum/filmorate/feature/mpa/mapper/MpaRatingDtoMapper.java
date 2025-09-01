package ru.yandex.practicum.filmorate.feature.mpa.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.feature.mpa.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;

@Component
public class MpaRatingDtoMapper {
    /**
     * In.
     */
    private MpaRating toDomain(MpaRatingDto dto) {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(dto.getId());
        mpaRating.setName(dto.getName());
        return mpaRating;
    }

    /**
     * Out.
     */
    public MpaRatingDto toDto(MpaRating mpaRating) {
        return createDto(mpaRating);
    }

    public List<MpaRatingDto> toDto(List<MpaRating> mpaRatings) {
        return mpaRatings.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    public MpaRatingDto createDto(MpaRating mpaRating) {
        MpaRatingDto dto = new MpaRatingDto();
        dto.setId(mpaRating.getId());
        dto.setName(mpaRating.getName());
        return dto;
    }
}
