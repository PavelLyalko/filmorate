package ru.yandex.practicum.Filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.Filmorate.model.Genre;
import ru.yandex.practicum.Filmorate.service.GenreService;
import ru.yandex.practicum.Filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Optional<Genre> getById(int id) {
        return genreStorage.getById(id);
    }

    @Override
    public boolean existsById(int id) {
        return genreStorage.existsById(id);
    }
}
