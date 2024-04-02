package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.intrfc.RatingStorage;
import ru.yandex.practicum.filmorate.dao.impl.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void shouldReturnAllRatings() {
        RatingStorage ratingStorage = new RatingDbStorage(jdbcTemplate);
        Collection<Rating> ratings = ratingStorage.getRating();

        assertEquals(5, ratings.size());
    }

    @Test void shouldReturnRatingById() {
        RatingStorage ratingStorage = new RatingDbStorage(jdbcTemplate);
        Rating rating = ratingStorage.getRatingById(1);
        assertEquals("G", rating.getName());
    }
}