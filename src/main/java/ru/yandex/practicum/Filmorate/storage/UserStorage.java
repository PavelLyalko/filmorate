package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void create(User user);

    void update(User updateUser);

    Collection<User> getUsers();

    User getUser(Long id);
}