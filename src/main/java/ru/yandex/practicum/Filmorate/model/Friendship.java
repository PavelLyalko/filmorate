package ru.yandex.practicum.Filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Friendship {
    private Long userId;
    private Long friendId;
    private String status;
    private LocalDateTime requestTime;
}
