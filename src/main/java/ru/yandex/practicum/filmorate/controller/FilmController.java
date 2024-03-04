package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    private int nextId = 1;
    private List<Film> films = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .create();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        return filmStorage.updateFilm(updatedFilm);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    private String createErrorResponse(String errorMessage) {
        return gson.toJson(new ErrorResponse("Bad Request", errorMessage));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            filmService.addLike(id, userId);
            return ResponseEntity.ok("Like added successfully");
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(errorResponse));
        } catch (ObjectNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Not Found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(errorResponse));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(errorResponse));
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        throw new ResourceNotFoundException("");
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "5") int count) {
        List<Film> popularFilms = (List<Film>) filmService.getPopularFilms(count);
        return ResponseEntity.ok(popularFilms);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmStorage.getFilmById(id);
    }
}
