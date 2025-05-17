package ru.yandex.practicum.Filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public void create(Film film) {
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
    public Optional<Film> getFilm(Long id) {
        if (!films.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(films.get(id));
    }
}
