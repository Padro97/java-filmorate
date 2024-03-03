package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;
    private final Map<Long, Long> likes;
    private Long id;

    public InMemoryFilmStorage() {
        likes = new HashMap<>();
        films = new HashMap<>();
        id = 0L;
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.info("'{}' movie was added to a library, the identifier is '{}'", film.getName(), film.getId());

        log.info("Films in the library after creation: {}", films);

        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.info("'{}' movie was updated in a library, the identifier is '{}'", film.getName(), film.getId());
            return film;
        } else {
            throw new ObjectNotFoundException("Attempt to update non-existing movie");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Attempt to reach non-existing movie with id '" + id + "'");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        log.info("There are '{}' movies in a library now", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (likes.containsKey(filmId)) {
            likes.put(filmId, userId);
        } else {
            throw new ObjectNotFoundException("Unable to add like to non-existing movie %d" + filmId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (likes.containsKey(filmId)) {
            likes.remove(filmId, userId);
        } else {
            throw new ObjectNotFoundException("No like were found from user " + userId + " to film " + filmId);
        }
    }

    @Override
    public int getLikesQuantity(Long filmId) {
        int sum = 0;
        if (likes.containsKey(filmId)) {
            for (Map.Entry<Long, Long> entry : likes.entrySet()) {
                Long film = entry.getKey();
                Long user = entry.getValue();
                if (film.equals(filmId)) {
                    sum += user;
                }
            }
        }
        return sum;
    }

    @Override
    public boolean isContains(Long id) {
        return false;
    }


    private void validate(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Incorrect release date");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Attempt to set an empty movie name");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Attempt to set duration less than zero");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Description increases 200 symbols or empty");
        }
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(++id);
            log.info("Movie identifier was set as '{}", film.getId());
        }
    }
}

