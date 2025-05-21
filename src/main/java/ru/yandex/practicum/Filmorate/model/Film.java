package ru.yandex.practicum.Filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> filmLikes;

    public void putLike(Long userId) {
        this.filmLikes.add(userId);
    }

    public void deleteLike(Long userId) {
        this.filmLikes.remove(userId);
    }
}
