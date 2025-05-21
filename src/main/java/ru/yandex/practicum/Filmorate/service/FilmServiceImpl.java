package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> ((Film) film).getFilmLikes().size()).reversed())
                .limit(count).toList();
    }

    @Override
    public Film getFilm(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден."));
    }

    @Override
    public void create(Film film) {
        filmStorage.create(film);
    }

    @Override
    public void update(Film updateFilm) {
        filmStorage.update(updateFilm);
    }

    @Override
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public void likedFilms(Long id, Long userId) {
        Film film = getFilm(id);
        film.putLike(userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Film film = getFilm(id);
        film.deleteLike(userId);
    }
}
