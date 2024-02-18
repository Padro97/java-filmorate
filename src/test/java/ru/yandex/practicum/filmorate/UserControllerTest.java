package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    @Test
    void testCreateUser_Success() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUser_InvalidEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserEmptyEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setName("dada");
        user.setEmail("");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserInvalidName() {
        UserController userController = new UserController();
        User user = new User();
        user.setName("");
        user.setEmail("s@");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserEmptyLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setName("dada");
        user.setEmail("dada@");
        user.setLogin("");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserInvalidLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setName("dada");
        user.setEmail("dada@");
        user.setLogin("ds ds");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserInvalidBirthday() {
        UserController userController = new UserController();
        User user = new User();
        user.setName("dada");
        user.setEmail("dada@");
        user.setLogin("dsds");
        user.setBirthday(LocalDate.now().plusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUser_UserFound() {
        UserController userController = new UserController();
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setEmail("test@example.com");
        existingUser.setLogin("test");
        existingUser.setBirthday(LocalDate.now().minusYears(20));

        userController.getAllUsers().add(existingUser);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updated");
        updatedUser.setBirthday(LocalDate.now().minusYears(25));

        ResponseEntity<Object> responseEntity = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserController userController = new UserController();
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updated");
        updatedUser.setBirthday(LocalDate.now().minusYears(25));

        ResponseEntity<Object> responseEntity = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}

