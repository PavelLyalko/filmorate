package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public Film getFilm(String id) {
        Optional<Film> film = filmStorage.getFilm(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id " + id + " не найден.");
        }
        return film.get();
    }
}
