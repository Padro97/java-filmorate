package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.AfterOrEqualDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    protected int id;
    @NotEmpty(message = "Имя не может быть пустым")
    protected String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    protected String description;
    @AfterOrEqualDate(value = "1895-12-28",
            message = "Дата релиза не может быть раньше 28 декабря 1895 года или неверный формат yyyy-mm-dd")
    protected String releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    protected int duration;
    protected Collection<Genre> genres;
    protected Rating mpa;
}