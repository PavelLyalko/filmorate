package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Optional<User> update(User updateUser);

    Collection<User> getUsers();

    Optional<User> getUserById(Long id);
}