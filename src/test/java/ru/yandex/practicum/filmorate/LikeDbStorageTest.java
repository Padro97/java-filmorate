package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.intrfc.LikeStorage;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void shouldDeleteLike() {
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        likeStorage.deleteLike(3, 1);

        assertEquals(2, likeStorage.getPopularFilms(3).size());
        likeStorage.addLike(3, 1);
    }

    @Test
    public void shouldAddLike() {
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        likeStorage.addLike(3, 2);

        assertEquals(3, likeStorage.getPopularFilms(3).size());
    }

    @Test
    public void shouldReturnMostPopularFilm() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        assertEquals(1, likeStorage.getPopularFilms(1).size());
        assertTrue(likeStorage.getPopularFilms(1).contains(filmStorage.findFilm(1)));
    }
}