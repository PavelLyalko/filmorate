package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    List<Film> getPopularFilms(int count);
    Film getFilm(Long id);

    void create(Film film);

    void update(Film updateFilm);

    Collection<Film> getFilms();

    void likedFilms(Long id, Long userId);

    void deleteLike(Long id, Long userId);
}
