package ru.yandex.practicum.filmorate.Exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectNotFoundException extends IllegalArgumentException {

    public ObjectNotFoundException(final String message) {
        super(message);
        log.error(message);
    }
}
