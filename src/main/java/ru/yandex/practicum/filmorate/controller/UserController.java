package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .create();

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.error("400 - Invalid email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid email"));
            }

            if (user.getId() < 1) {
                log.error("400 - Invalid user ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid user ID"));
            }

            if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.error("400 - Invalid login");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid login"));
            }

            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
                user.setId(2);
            } else if (user.getName().length() > 200) {
                log.error("400 - Invalid name length");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid name length"));
            }

            if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
                log.error("400 - Invalid birthday");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid birthday"));
            }

            users.add(user);
            log.info("200 - User created successfully");
            String jsonResponse = gson.toJson(user);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            log.error("500 - Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Internal Server Error"));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@RequestBody User updatedUser) {
        try {
            for (User user : users) {
                if (user.getId() == updatedUser.getId()) {
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("200 - User updated successfully");

                    // Преобразование объекта в JSON с использованием Gson
                    String jsonResponse = gson.toJson(user);

                    // Возвращаем успешный ответ с JSON в теле
                    return ResponseEntity.ok(jsonResponse);
                }
            }

            // Если пользователь не найден, возвращаем 404 с сообщением об ошибке
            log.error("404 - User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found", "User not found"));
        } catch (Exception e) {
            log.error("500 - Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error", "Internal Server Error"));
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    private String createErrorResponse(String errorMessage) {
        return gson.toJson(new ErrorResponse("Bad Request", errorMessage));
    }
}
