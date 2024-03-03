package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser_Success() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUser_InvalidEmail() {

        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("test");
        user.setBirthday(LocalDate.now().minusYears(20));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUserEmptyEmail() {

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

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updated");
        updatedUser.setBirthday(LocalDate.now().minusYears(25));

        ResponseEntity<Object> responseEntity = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testAddFriend() {
        // Создаем двух пользователей
        User user1 = createUser("user1@example.com", "user1", "User 1");
        User user2 = createUser("user2@example.com", "user2", "User 2");

        // Добавляем пользователей в систему
        userController.createUser(user1);
        userController.createUser(user2);

        // Получаем ID пользователей после их создания
        int userId1 = user1.getId();
        int userId2 = user2.getId();

        // Добавляем второго пользователя в друзья первому
        ResponseEntity<Object> responseEntity = userController.addFriend(userId1, userId2);

        // Проверяем, что операция прошла успешно
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Получаем обновленные данные о пользователях
        User updatedUser1 = userService.getUserById(userId1);
        User updatedUser2 = userService.getUserById(userId2);

        // Проверяем, что второй пользователь добавлен в друзья первому
        assertTrue(updatedUser1.getFriends().contains(userId2));
        assertTrue(updatedUser2.getFriends().contains(userId1));
    }

    private User createUser(String email, String login, String name) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        return user;
    }
}

