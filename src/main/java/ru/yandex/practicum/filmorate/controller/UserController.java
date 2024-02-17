package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;




@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.error("400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (user.getId() < 1) {
                log.error("400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.error("400 - Invalid login");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login");
            }

            if (user.getName() == null || user.getName().isEmpty()) {
                log.error("400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
                log.error("400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            users.add(user);
            log.info("200");
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Произошла ошибка при создании пользователя", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        try {
            for (User user : users) {
                if (user.getId() == id) {
                    user.setId(id);
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("Пользователь успешно обновлен: {}", user);
                    return ResponseEntity.ok(user);
                }
            }

            log.error("Ошибка: Пользователь с указанным ID не найден");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении пользователя", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
