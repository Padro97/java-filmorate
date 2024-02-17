package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film {
    private static int nextId = 1;

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film() {
        this.id = nextId++;
    }
}
