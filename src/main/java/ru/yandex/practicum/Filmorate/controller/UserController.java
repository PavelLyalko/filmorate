package ru.yandex.practicum.Filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.Filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        userService.create(user);
        log.debug("Пользователь с id {} успешно добавлен", user.getId());

        return user;
    }

    @PutMapping
    public User update(@RequestBody User updateUser) {
        userService.update(updateUser);
        log.debug("Пользователь с id {} успешно обновлен", updateUser.getId());

        return updateUser;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Получение всех пользователей: {}", userService.getUsers());
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("получения пользователя по Id: {}", id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addUserInFriendList(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return new ResponseEntity<>("Новый друг успешно добавлен", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeUserFromFriendList(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("Пользователь с Id: {}, удалил пользователя с Id {} из друзей", id, friendId);
        userService.deleteFriend(id, friendId);
        return new ResponseEntity<>("Друг успешно удален", HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriendList(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getEachFriendList(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getEachFriendList(id, otherId);
    }
}
