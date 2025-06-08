package ru.yandex.practicum.Filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.Filmorate.model.Mpa;
import ru.yandex.practicum.Filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryMpaStorage implements MpaStorage {
    private static final List<Mpa> MPAS = List.of(
            new Mpa(1, "G"),
            new Mpa(2, "PG"),
            new Mpa(3, "PG-13"),
            new Mpa(4, "R"),
            new Mpa(5, "NC-17")
    );

    @Override
    public List<Mpa> getAll() {
        return new ArrayList<>(MPAS);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        return MPAS.stream().filter(m -> m.getId() == id).findFirst();
    }
}
