package ru.yandex.practicum.Filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
}
