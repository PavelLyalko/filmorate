package ru.yandex.practicum.Filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.Filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.Filmorate.model.Genre;

import java.util.List;



@Primary
@RequiredArgsConstructor
@Repository
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper = new GenreRowMapper();

    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    public Genre getById(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, genreRowMapper, id);
    }

}
