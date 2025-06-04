package ru.yandex.practicum.Filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.Filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.Filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.Filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.model.Genre;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Primary
@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper = new FilmRowMapper();

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genres = film.getGenres();
            genres.stream()
                    .map(Genre::getId)
                    .forEach(this::existsById);

            jdbcTemplate.batchUpdate(
                    "INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Genre genre = (Genre) genres.toArray()[i];
                            ps.setLong(1, id);
                            ps.setLong(2, genre.getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return genres.size();
                        }
                    }
            );
        }

        return getFilmById(id).orElseThrow(() -> new RuntimeException("Film not found after insert"));
    }

    @Override
    public Optional<Film> update(Film updateFilm) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                updateFilm.getName(),
                updateFilm.getDescription(),
                java.sql.Date.valueOf(updateFilm.getReleaseDate()),
                updateFilm.getDuration(),
                updateFilm.getMpa().getId(),
                updateFilm.getId());

        if (rows == 0) {
            return Optional.empty();
        }

        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE film_id = ?", updateFilm.getId());
        if (updateFilm.getGenres() != null && !updateFilm.getGenres().isEmpty()) {
            for (Genre genre : updateFilm.getGenres()) {
                if (!existsById(genre.getId())) {
                    throw new GenreNotFoundException(genre.getId());
                }
                jdbcTemplate.update("INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)", updateFilm.getId(), genre.getId());
            }
        }

        return getFilmById(updateFilm.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT f.*, m.name as mpa_name FROM FILMS f JOIN MPA m ON f.mpa_id = m.id";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);

        for (Film film : films) {
            List<Integer> genreIds = jdbcTemplate.queryForList(
                    "SELECT genre_id FROM FILM_GENRES WHERE film_id = ?", Integer.class, film.getId());
            Set<Genre> genres = new LinkedHashSet<>();
            for (Integer genreId : genreIds) {
                getById(genreId).ifPresent(genres::add);
            }
            film.setGenres(genres);

            List<Long> likes = jdbcTemplate.queryForList(
                    "SELECT user_id FROM FILM_LIKES WHERE film_id = ?", Long.class, film.getId());
            film.setFilmLikes(new HashSet<>(likes));
        }
        return films;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        try {
            String sql = "SELECT f.*, m.name as mpa_name FROM FILMS f JOIN MPA m ON f.mpa_id = m.id WHERE f.id = ?";
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper, id);

            List<Integer> genreIds = jdbcTemplate.queryForList(
                    "SELECT genre_id FROM FILM_GENRES WHERE film_id = ?", Integer.class, id);
            Set<Genre> genres = new LinkedHashSet<>();
            for (Integer genreId : genreIds) {
                getById(genreId).ifPresent(genres::add);
            }
            film.setGenres(genres);

            List<Long> likes = jdbcTemplate.queryForList(
                    "SELECT user_id FROM FILM_LIKES WHERE film_id = ?", Long.class, id);
            film.setFilmLikes(new HashSet<>(likes));

            return Optional.of(film);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Optional<Genre> getById(Integer id) {
        Genre genre = jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE id = ?", new GenreRowMapper(), id);
        return Optional.ofNullable(genre);
    }

    private boolean existsById(Integer genreId) {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM GENRES WHERE id = ?", new GenreRowMapper(), genreId);

        if (genres.isEmpty()) {
            throw new GenreNotFoundException(genreId);
        }

        return true;
    }
}
