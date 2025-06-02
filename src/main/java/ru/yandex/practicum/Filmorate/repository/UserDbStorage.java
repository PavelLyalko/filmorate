package ru.yandex.practicum.Filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.Filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USERS (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            if (user.getBirthday() != null) {
                ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> update(User updateUser) {
        if (!existsById(updateUser.getId())) {
            return Optional.empty();
        }
        int rows = jdbcTemplate.update(
                "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                updateUser.getEmail(), updateUser.getLogin(), updateUser.getName(),
                updateUser.getBirthday() != null ? java.sql.Date.valueOf(updateUser.getBirthday()) : null,
                updateUser.getId());
        return rows > 0 ? Optional.of(updateUser) : Optional.empty();
    }

    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", userRowMapper);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            String sql = "SELECT * FROM USERS WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM USERS WHERE id = ?", id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
