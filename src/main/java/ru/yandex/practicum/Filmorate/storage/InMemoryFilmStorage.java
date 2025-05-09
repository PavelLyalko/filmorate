package ru.yandex.practicum.Filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public void create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film updateFilm) {
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
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(String id) {
        if (!films.containsKey(Long.parseLong(id))) {
            throw new NotFoundException("Фильм с id " + id + " не найден.");
        }
        return films.get(Long.parseLong(id));
    }

}
