package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public List<Film> getPopularFilms(String count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> ((Film) film).getFilmLikes().size()).reversed())
                .limit(Integer.parseInt(count)).toList();
    }
}
