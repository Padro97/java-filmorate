package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        Film newFilm = new Film();
        Rating rating = new Rating();
        newFilm.setId(1);
        newFilm.setName("aaa");
        newFilm.setDescription("aaa");
        newFilm.setReleaseDate("2022-11-11");
        newFilm.setDuration(60);
        rating.setId(2);
        newFilm.setMpa(rating);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Film savedFilm = filmStorage.findFilm(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testUpdateFilm() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Film film = filmStorage.findFilm(2);
        Film updatedFilm = new Film();
        Rating rating = new Rating();
        updatedFilm.setId(2);
        updatedFilm.setName("ff");
        updatedFilm.setDescription("gn");
        updatedFilm.setReleaseDate("2000-11-09");
        updatedFilm.setDuration(100);
        rating.setId(2);
        updatedFilm.setMpa(rating);
        filmStorage.updateFilm(updatedFilm);

        Film savedFilm = filmStorage.findFilm(2);

        filmStorage.updateFilm(film);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

    @Test
    public void testDeleteFilm() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Film updatedFilm = new Film();
        Rating rating = new Rating();
        updatedFilm.setId(4);
        updatedFilm.setName("afa");
        updatedFilm.setDescription("fafa");
        updatedFilm.setReleaseDate("2021-12-10");
        updatedFilm.setDuration(10);
        rating.setId(2);
        updatedFilm.setMpa(rating);
        filmStorage.createFilm(updatedFilm);
        Film savedFilm = filmStorage.findFilm(4);
        filmStorage.deleteFilm(4);

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Film film = filmStorage.findFilm(4);
                    }
                });

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
        assertEquals("Фильм не найден.", exception.getMessage());
    }

    @Test
    public void findAllFilms() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Collection<Film> dbFilms = filmStorage.findAll();

        List<Film> films = new ArrayList<>(dbFilms);

        assertEquals(3, films.size());
    }
}
