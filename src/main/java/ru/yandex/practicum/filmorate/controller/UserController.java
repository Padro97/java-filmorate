package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    private int nextId = 1;

    private List<User> users = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .create();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getUsers();
    }

    private String createErrorResponse(String errorMessage) {
        return gson.toJson(new ErrorResponse("Bad Request", errorMessage));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            if (friendId <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Invalid friendId"));
            }

            userService.addFriend(id, friendId);
            return ResponseEntity.ok("Friend added successfully");
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Friend not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Internal Server Error"));
        }
    }



    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            userService.removeFriend(id, friendId);
            return ResponseEntity.ok("Friend removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        try {
            List<User> friends = userService.getFriends(id);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        try {
            List<Long> commonFriendsIds = userService.getCommonFriends(id, otherId);
            List<User> commonFriends = commonFriendsIds.stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(commonFriends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userStorage.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




}