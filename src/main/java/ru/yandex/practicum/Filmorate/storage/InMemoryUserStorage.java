package ru.yandex.practicum.Filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<java.lang.Long, User> users = new HashMap<>();

    @Override
    public void create(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User updateUser) {
        User user = users.get(updateUser.getId());
        if (updateUser.getBirthday() != null) {
            user.setBirthday(updateUser.getBirthday());
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        if (updateUser.getLogin() != null) {
            user.setLogin(updateUser.getLogin());
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.of(users.get(id));
    }
}
