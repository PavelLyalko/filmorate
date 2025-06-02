package ru.yandex.practicum.Filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.Filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.Filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MpaDbStorage {

    private final MpaRowMapper mpaRowMapper = new MpaRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> findAll() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, mpaRowMapper);
    }

    public Mpa findById(int id) {
        String sql = "SELECT * FROM MPA WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mpaRowMapper, id);
    }
}
