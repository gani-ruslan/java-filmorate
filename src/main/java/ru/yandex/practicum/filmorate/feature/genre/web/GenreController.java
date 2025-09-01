package ru.yandex.practicum.filmorate.feature.genre.web;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.feature.genre.dto.GenreDto;
import ru.yandex.practicum.filmorate.feature.genre.mapper.GenreDtoMapper;
import ru.yandex.practicum.filmorate.feature.genre.service.GenreService;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreDtoMapper mapper;

    public GenreController(GenreService genreService, GenreDtoMapper mapper) {
        this.genreService = genreService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<GenreDto> findAll() {
        return mapper.toDto(genreService.findAll());
    }

    @GetMapping("/{id}")
    public GenreDto findById(@PathVariable Integer id) {
        return mapper.toDto(genreService.findById(id));
    }
}
