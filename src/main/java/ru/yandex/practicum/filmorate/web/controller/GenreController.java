package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.web.dto.GenreDto;
import ru.yandex.practicum.filmorate.web.mapper.GenreDtoMapper;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreDto> getAll() {
        return service.findAll().stream().map(GenreDtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public GenreDto getById(@PathVariable int id) {
        return GenreDtoMapper.toDto(service.findById(id));
    }
}
