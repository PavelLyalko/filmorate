package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> getAll();

    Optional<Mpa> getById(int id);
}
