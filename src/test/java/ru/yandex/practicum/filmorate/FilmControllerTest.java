package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    @Test
    void testAddFilmSuccess() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("sadsada");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.now());
        film.setName("dadadada");

        ResponseEntity<String> responseEntity = filmController.addFilm(film);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testAddFilmLargeDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.now());
        film.setName("dadadada");

        ResponseEntity<String> responseEntity = filmController.addFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testAddFilmEmtyName() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("fdsfsd");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.now());
        film.setName("");

        ResponseEntity<String> responseEntity = filmController.addFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testAddFilmInvalidReleaseDate() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("fdsfsd");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.now().minusYears(300));
        film.setName("fsd");

        ResponseEntity<String> responseEntity = filmController.addFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testAddFilmInvalidDuration() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("fdsfsd");
        film.setDuration(-10);
        film.setReleaseDate(LocalDate.now());
        film.setName("da");

        ResponseEntity<String> responseEntity = filmController.addFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
