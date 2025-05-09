package ru.yandex.practicum.Filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    private FilmStorage filmStorage;

    @Override
    public void putLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId + "").getFilmLikes().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId + "").getFilmLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(String count) {
        if (count == null) count = "10";
        List<Film> films = new ArrayList<>(filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getFilmLikes().size()))
                .limit(Integer.parseInt(count)).toList());
        Collections.reverse(films);
        return films;
    }
}
