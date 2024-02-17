package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import com.google.gson.Gson;
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
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(user);
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.error("400 - Invalid email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }

            if (user.getId() < 1) {
                log.error("400 - Invalid user ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }

            if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.error("400 - Invalid login");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);

            }

            if (user.getName() == null || user.getName().isEmpty()) {
                log.error("400 - Invalid name");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }

            if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
                log.error("400 - Invalid birthday");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }

            users.add(user);
            log.info("200 - User created successfully");
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            log.error("500 - Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User updatedUser) {
        try {
            for (User user : users) {
                if (user.getId() == updatedUser.getId()) {
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("Пользователь успешно обновлен: {}", user);
                    return ResponseEntity.ok("User updated successfully");
                }
            }

            log.error("Ошибка: Пользователь с указанным ID не найден");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении пользователя", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
