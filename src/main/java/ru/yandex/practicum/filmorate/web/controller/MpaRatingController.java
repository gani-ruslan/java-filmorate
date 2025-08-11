package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.web.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.web.mapper.MpaRatingDtoMapper;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService service;

    MpaRatingController(MpaRatingService service) {
        this.service = service;
    }

    @GetMapping
    public List<MpaRatingDto> getAll() {
        return service.findAll().stream().map(MpaRatingDtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public MpaRatingDto getById(@PathVariable int id) {
        return MpaRatingDtoMapper.toDto(service.findById(id));
    }
}
