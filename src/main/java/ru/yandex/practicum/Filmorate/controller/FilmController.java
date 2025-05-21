package ru.yandex.practicum.Filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.Film;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.Filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        filmService.create(film);
        log.debug("Фильм с id {} успешно добавлен.", film.getId());

        return ResponseEntity.ok(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        filmService.update(updateFilm);
        log.debug("Фильм с id {} успешно обновлен.", updateFilm.getId());

        return updateFilm;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("Получение всех фильмов: {}", filmService.getFilms());

        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.debug("Получения фильма по Id: {}", id);
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> likedFilms(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Пользователь с Id {}, ставит лайк фильму с Id {}", id, userId);
        filmService.likedFilms(id, userId);
        return new ResponseEntity<>("Лайк успешно поставлен", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Пользователь с Id {}, удалил лайк у фильма с Id {}", id, userId);
        filmService.deleteLike(id, userId);
        return new ResponseEntity<>("Лайк успешно удален.", HttpStatus.OK);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.debug("Получение популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}
