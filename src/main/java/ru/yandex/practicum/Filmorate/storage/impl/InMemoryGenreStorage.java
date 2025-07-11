package ru.yandex.practicum.Filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.model.Genre;
import ru.yandex.practicum.Filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryGenreStorage implements GenreStorage {
    private static final List<Genre> GENRES = List.of(
            new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(6, "Боевик")
    );

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(GENRES);
    }

    @Override
    public Optional<Genre> getById(Integer id) {
        return GENRES.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(Integer genreId) {
        return GENRES.stream()
                .anyMatch(g -> g.getId().equals(genreId));
    }

    @Override
    public Set<Genre> resolveGenres(Set<Genre> input) {
        if (input == null) return new LinkedHashSet<>();
        Set<Integer> ids = new LinkedHashSet<>();
        for (Genre g : input) {
            if (g != null && g.getId() != null) {
                ids.add(g.getId());
            }
        }
        Set<Genre> result = new LinkedHashSet<>();
        for (Integer id : ids) {
            getById(id).ifPresent(result::add);
        }
        return result;
    }
}
