package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    protected Comparator<Film> comparator = new Comparator<>() {
        @Override
        public int compare(Film o1, Film o2) {
            if (o1.getLikes() == null || o2.getLikes() == null) {
                if (o1.getLikes() == null) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return o2.getLikes().size() - o1.getLikes().size();
        }
    };

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        userStorage.findUser(userId);

        film.addLike(userId);

        filmStorage.updateFilm(film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        userStorage.findUser(userId);

        film.deleteLike(userId);

        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}