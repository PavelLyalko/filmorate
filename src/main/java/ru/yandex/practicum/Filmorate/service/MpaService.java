package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaService {
    List<Mpa> getAll();

    Optional<Mpa> getById(int id);
}
