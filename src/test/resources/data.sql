INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
VALUES ('user@email.ru', 'padro', 'Nikita Polyanin', '2004-11-11'),
       ('np@yandex.ru', 'dad', 'fadd', '2001-10-11'),
       ('zsdaa@fsddfsd.ru', 'Lasad', 'braun', '2000-01-01');

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
VALUES ( 'aaa', 'aaa', '2022-11-11', 60, 2),
       ('Sun', 'good', '2012-12-30', 50, 1),
       ( 'ASA', 'good', '2000-06-08', 50, 5);

MERGE INTO RATINGS (RATING_ID, RATING_NAME) VALUES (1,'G'),
                                                   (2, 'PG'),
                                                   (3, 'PG-13'),
                                                   (4, 'R'),
                                                   (5, 'NC-17');

MERGE INTO GENRES (GENRE_ID, GENRE_NAME) VALUES (1, 'Комедия'),
                                                (2, 'Драма'),
                                                (3, 'Мультфильм'),
                                                (4, 'Триллер'),
                                                (5, 'Документальный'),
                                                (6, 'Боевик');

INSERT INTO FRIENDS (USER_ID, FRIEND_ID)
VALUES (1, 3),
       (2, 3);

INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (1, 1),
                                            (1, 2),
                                            (1, 3),
                                            (2, 1),
                                            (2, 2),
                                            (3, 1);