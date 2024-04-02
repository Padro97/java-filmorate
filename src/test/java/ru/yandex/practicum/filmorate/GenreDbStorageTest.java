package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.intrfc.GenreStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void shouldReturnAllGenres() {
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        assertEquals(6, genreStorage.getGenres().size());
    }

    @Test
    public void shouldReturnGenreById() {
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        assertEquals("Драма", genreStorage.getGenreById(2).getName());
    }
}