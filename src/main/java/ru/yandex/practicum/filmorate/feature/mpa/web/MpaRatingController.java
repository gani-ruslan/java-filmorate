package ru.yandex.practicum.filmorate.feature.mpa.web;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.feature.mpa.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.feature.mpa.mapper.MpaRatingDtoMapper;
import ru.yandex.practicum.filmorate.feature.mpa.service.MpaRatingService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService ratingService;
    private final MpaRatingDtoMapper mapper;

    MpaRatingController(MpaRatingService ratingService, MpaRatingDtoMapper mapper) {
        this.ratingService = ratingService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<MpaRatingDto> getAll() {
        return mapper.toDto(ratingService.findAll());
    }

    @GetMapping("/{id}")
    public MpaRatingDto getById(@PathVariable int id) {
        return mapper.toDto(ratingService.findById(id));
    }
}
