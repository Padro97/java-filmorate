package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    protected int id;
    @NotEmpty(message = "Почта не может быть пустой")
    @Email(message = "Почта не соответствует формату")
    protected String email;
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    @NotEmpty(message = "Логин не может быть пустым")
    protected String login;
    protected String name;
    @PastOrPresent(message = "День рождения не может быть в будущем")
    protected LocalDate birthday;
}