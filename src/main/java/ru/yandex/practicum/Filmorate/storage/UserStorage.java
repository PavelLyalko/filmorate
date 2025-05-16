package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    void create(User user);

    void update(User updateUser);

    Collection<User> getUsers();

    Optional<User> getUser(Long id);
}