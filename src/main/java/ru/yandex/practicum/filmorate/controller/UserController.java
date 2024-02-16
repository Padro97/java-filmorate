package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.error("Ошибка: Электронная почта не может быть пустой и должна содержать символ @");
                return null;
            }

            if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.error("Ошибка: Логин не может быть пустым и содержать пробелы");
                return null;
            }

            if (user.getName() == null || user.getName().isEmpty()) {
                log.error("Ошибка: Имя пользователя не может быть пустым");
                return null;
            }

            if (user.getBirthday() != null && user.getBirthday().after(new Date(2024, 2, 16))) {
                log.error("Ошибка: Дата рождения не может быть в будущем");
                return null;
            }

            users.add(user);
            log.info("Пользователь успешно создан: {}", user);
            return user;
        } catch (Exception e) {
            log.error("Произошла ошибка при создании пользователя", e);
            return null;
        }
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        try {
            for (User user : users) {
                if (user.getId() == id) {
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("Пользователь успешно обновлен: {}", user);
                    return user;
                }
            }

            log.error("Ошибка: Пользователь с указанным ID не найден");
            return null;
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении пользователя", e);
            return null;
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return users;
    }
}
