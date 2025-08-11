-- Удаляем зависимости, чтобы не было конфликтов с FK
DELETE FROM film_genres;
DELETE FROM film_likes;
DELETE FROM friendships;

-- Очищаем основные таблицы
DELETE FROM films;
DELETE FROM users;
DELETE FROM genres;
DELETE FROM mpa_rating;

-- Сбрасываем автоинкремент (H2)
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpa_rating ALTER COLUMN id RESTART WITH 1;

-- Заполнение таблицы genres
MERGE INTO genres KEY(id) VALUES (1, 'Комедия');
MERGE INTO genres KEY(id) VALUES (2, 'Драма');
MERGE INTO genres KEY(id) VALUES (3, 'Мультфильм');
MERGE INTO genres KEY(id) VALUES (4, 'Триллер');
MERGE INTO genres KEY(id) VALUES (5, 'Документальный');
MERGE INTO genres KEY(id) VALUES (6, 'Боевик');

-- Заполнение таблицы mpa_ratings
MERGE INTO mpa_rating KEY(id) VALUES (1, 'G');
MERGE INTO mpa_rating KEY(id) VALUES (2, 'PG');
MERGE INTO mpa_rating KEY(id) VALUES (3, 'PG-13');
MERGE INTO mpa_rating KEY(id) VALUES (4, 'R');
MERGE INTO mpa_rating KEY(id) VALUES (5, 'NC-17');
