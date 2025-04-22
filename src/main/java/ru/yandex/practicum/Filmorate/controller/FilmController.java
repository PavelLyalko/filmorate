package ru.yandex.practicum.Filmorate.controller;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.Filmorate.model.Film;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Название не может быть пустым. Film: {}", film);
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200 || film.getDescription().isEmpty()) {
            log.error("Максимальная длина описания — 200 символов. Film: {}", film);
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года. Film: {}", film);
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            log.error("Продолжительность фильма должна быть положительным числом. Film: {}", film);
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        films.put(film.getId(), film);
        log.debug("Фильм с id {} успешно добавлен.", film.getId());
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public Film update(@RequestBody Film updateFilm) {
        Film film = films.get(updateFilm.getId());
        if (updateFilm.getDescription() != null) {
            film.setDescription(updateFilm.getDescription());
        }
        if (updateFilm.getName() != null) {
            film.setName(updateFilm.getName());
        }
        if (updateFilm.getDuration() != null) {
            film.setDuration(updateFilm.getDuration());
        }
        if (updateFilm.getReleaseDate() != null) {
            film.setReleaseDate(updateFilm.getReleaseDate());
        }
        log.debug("Фильм с id {} успешно обновлен.", film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("Получение всех фильмов: {}", films.values());
        return films.values();
    }
}
