package ru.yandex.practicum.Filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.model.Mpa;
import ru.yandex.practicum.Filmorate.service.MpaService;
import ru.yandex.practicum.Filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Optional<Mpa> getById(int id) {
        return mpaStorage.getById(id);
    }
}
