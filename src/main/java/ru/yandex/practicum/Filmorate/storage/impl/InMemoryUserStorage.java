package ru.yandex.practicum.Filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Deprecated
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(User updateUser) {
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
        return Optional.of(user);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(users.get(id));
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
