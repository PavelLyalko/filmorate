package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void putLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(String count);
}
