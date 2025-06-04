package ru.yandex.practicum.Filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.Filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("id"));
        mpa.setName(rs.getString("name"));

        return mpa;
    }
}
