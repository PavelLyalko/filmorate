package ru.yandex.practicum.Filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(Integer genreId) {
        super(String.format("Жанр с id %d не существует", genreId));
    }
}
