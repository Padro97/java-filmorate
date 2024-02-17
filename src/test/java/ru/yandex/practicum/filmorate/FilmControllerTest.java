package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController filmController;
    String dateString = "1990-02-16";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(dateString, formatter);

    @BeforeEach
    public void setup() {
        filmController = new FilmController();
    }

    @Test
    public void testAddFilmWithNullInput() {

        Film result = filmController.addFilm(null);
        assertEquals(null, result);
    }

    @Test
    public void testAddFilmWithEmptyName() {
        Film filmWithEmptyName = new Film();
        Film result = filmController.addFilm(null);
        assertEquals(null, result);
    }

    @Test
    public void testAddFilmWithLongDescription() {
        Film filmWithLongDescription = new Film();
        filmWithLongDescription.setName("Test Film");
        filmWithLongDescription.setDescription("This is a very long description exceeding the maximum allowed length of 200 characters. This description is longer than 200 characters.ффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффффф");
        filmWithLongDescription.setDuration(10);
        filmWithLongDescription.setId(1);
        filmWithLongDescription.setReleaseDate(date);
        Film result = filmController.addFilm(filmWithLongDescription);
        assertEquals(null, result);
    }

    @Test
    public void testAddFilmWithInvalidReleaseDate() {
        String invalidDateString = "1800-02-16";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate invalidDate = LocalDate.parse(invalidDateString, formatter);
        Film filmWithInvalidReleaseDate = new Film();
        filmWithInvalidReleaseDate.setName("Test Film");
        filmWithInvalidReleaseDate.setReleaseDate(invalidDate);
        filmWithInvalidReleaseDate.setId(1);
        filmWithInvalidReleaseDate.setDuration(10);
        filmWithInvalidReleaseDate.setDescription("aaaaaaa");
        Film result = filmController.addFilm(filmWithInvalidReleaseDate);
        assertEquals(null, result);
    }

    @Test
    public void testAddFilmWithNegativeDuration() {
        Film filmWithNegativeDuration = new Film();
        filmWithNegativeDuration.setName("Test Film");
        filmWithNegativeDuration.setId(1);
        filmWithNegativeDuration.setReleaseDate(date);
        filmWithNegativeDuration.setDuration(-1);
        Film result = filmController.addFilm(filmWithNegativeDuration);
        assertEquals(null, result);
    }
}
