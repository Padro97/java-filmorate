package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    String dateString = "1990-02-16";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(dateString, formatter);

    @BeforeEach
    public void setup() {
        userController = new UserController();
    }

    @Test
    public void testCreateUserWithValidData() {
        User validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testuser");
        validUser.setName("Test User");
        validUser.setBirthday(date);

        User createdUser = userController.createUser(validUser);

        assertNotNull(createdUser, "User should be created");
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        User userWithInvalidEmail = new User();
        userWithInvalidEmail.setEmail("invalidemail");
        userWithInvalidEmail.setName("Name");
        userWithInvalidEmail.setBirthday(date);
        userWithInvalidEmail.setLogin("Logut");
        User result = userController.createUser(userWithInvalidEmail);

        assertEquals(null, result);
    }

    @Test
    public void testCreateUserWithInvalidLogin() {
        User userWithInvalidLogin = new User();
        userWithInvalidLogin.setLogin("user with spaces");
        userWithInvalidLogin.setEmail("email@");
        userWithInvalidLogin.setId(1);
        userWithInvalidLogin.setName("aa");
        userWithInvalidLogin.setBirthday(date);
        User result = userController.createUser(userWithInvalidLogin);
        assertEquals(null, result);
    }

    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User();
        user.setLogin("user");
        user.setEmail("email@");
        user.setName("");
        user.setId(1);
        user.setBirthday(date);
        User result = userController.createUser(user);
        assertEquals(null, result);
    }

    @Test
    public void testCreateUserWithFutureBirthday() {
        User user = new User();
        String invalidDateS = "2145-02-16";
        LocalDate invalidDate = LocalDate.parse(invalidDateS, formatter);
        user.setLogin("user");
        user.setEmail("email@");
        user.setName("sda");
        user.setId(1);
        user.setBirthday(invalidDate);
        User result = userController.createUser(user);
        assertEquals(null, result);
    }


}
