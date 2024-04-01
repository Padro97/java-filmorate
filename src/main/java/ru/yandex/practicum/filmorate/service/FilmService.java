package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.intrfc.FilmsGenreStorage;
import ru.yandex.practicum.filmorate.dao.intrfc.GenreStorage;
import ru.yandex.practicum.filmorate.dao.intrfc.LikeStorage;
import ru.yandex.practicum.filmorate.dao.intrfc.RatingStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.*;

@Service
public class FilmService {
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmsGenreStorage filmsGenreStorage;

    @Autowired
    public FilmService(LikeStorage likeStorage,
                       GenreStorage genreStorage,
                       RatingStorage ratingStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       FilmsGenreStorage filmsGenreStorage) {
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmsGenreStorage = filmsGenreStorage;
    }

    public Collection<Film> findAll() {
        return addGenreAndRating(filmStorage.findAll());
    }

    public Film findFilm(int id) {
        return addGenreAndRating(Collections.singletonList(filmStorage.findFilm(id))).iterator().next();
    }

    public Film create(Film film) {
        if (film.getMpa().getId() > ratingStorage.getRating().size()) {
            throw new ValidationException("Рейтинг с id=" + film.getMpa().getId() + "отсутствует");
        }

        if (film.getGenres() != null) {
            int allGenres = genreStorage.getGenres().size();
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > allGenres) {
                    throw new ValidationException("Жанр с id=" + genre.getId() + "отсутствует");
                }
            }
        }

        Film insertedFilm = filmStorage.createFilm(film);
        filmsGenreStorage.setFilmGenres(insertedFilm);

        return addGenreAndRating(Collections.singletonList(insertedFilm)).iterator().next();
    }

    private Collection<Film> addGenreAndRating(Collection<Film> films) {
        List<Genre> genres = new ArrayList<>(genreStorage.getGenres());
        List<Rating> ratings = new ArrayList<>(ratingStorage.getRating());
        Map<Integer, List<Integer>> filmsGenres = filmsGenreStorage.getGenresOfFilmsById(films);

        for (Film film : films) {
            List<Integer> genresId = filmsGenres.get(film.getId());
            List<Genre> filmGenres = new ArrayList<>();
            if (genresId != null) {
                for (Integer id : genresId) {
                    filmGenres.add(genres.get(id - 1));
                }
            }

            film.setGenres(filmGenres);
            if (film.getMpa() != null) {
                film.setMpa(ratings.get(film.getMpa().getId() - 1));
            }
        }
        return films;
    }

    public Film update(Film film) {
        return addGenreAndRating(Collections.singletonList(filmStorage.updateFilm(film))).iterator().next();
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return addGenreAndRating(likeStorage.getPopularFilms(count));
    }

    public Collection<Rating> getRating() {
        return ratingStorage.getRating();
    }

    public Rating getRatingById(int ratingId) {
        return ratingStorage.getRatingById(ratingId);
    }

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId);
    }
}
