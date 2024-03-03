package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final Map<Long, Set<Long>> likes;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        this.likes = new HashMap<>();
    }

    public void addLike(Long filmId, Long userId) {
        likes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }


    public void removeLike(Long filmId, Long userId) {
        if (!likes.containsKey(filmId) || !likes.get(filmId).contains(userId)) {
            throw new ObjectNotFoundException("User did not like the film");
        }

        likes.get(filmId).remove(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        Set<Long> popularFilmIds = likes.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(count != null ? count : 10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return filmStorage.getFilms().stream()
                .filter(film -> popularFilmIds.contains(film.getId()))
                .collect(Collectors.toList());
    }



    public Film getFilmById(Long id) {
        Optional<Film> optionalFilm = filmStorage.getFilms().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();

        return optionalFilm.orElse(null);
    }


}

