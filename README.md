## java-filmorate

## Описание
Проект **Filmorate** теперь хранит данные в реляционной БД **H2** (JDBC, без JPA).  
Схема нормализована до **3NF** и покрывает:
- пользователей, фильмы, жанры, рейтинги MPA;
- **одностороннюю** дружбу между пользователями;
- лайки фильмов;
- связь «многие-ко-многим» между фильмами и жанрами.

---

## Технологии и режимы БД
- **H2 (file mode)** для рабочего режима: данные сохраняются между перезапусками.
- **H2 (in-memory)** для интеграционных тестов.
- Скрипты инициализации: `schema.sql` + `data.sql` (выполняются на старте, `spring.sql.init.mode=always`).
- Активный профиль: `spring.profiles.active=jdbc`.

**Консоль H2:** `http://localhost:8080/h2-console`  
**JDBC URL:** `jdbc:h2:file:./db/filmorate`  
**User/Password:** `sa` / `password` (по умолчанию)

---

## Схема базы данных

![ER Diagram](https://i.postimg.cc/xCJKVhHt/Film-Rating-1.png)  
Ссылка на проект: [dbdiagram.io](https://dbdiagram.io/d/Film-Rating-689462a6dd90d17865dde602)

**Фактические таблицы в проекте:**
1. **users** — пользователи приложения
2. **films** — фильмы (название, описание, дата релиза, длительность, рейтинг MPA)
3. **genres** — справочник жанров
4. **mpa_rating** — справочник возрастных рейтингов MPA
5. **film_genres** — связь фильмов и жанров (m:n)
6. **film_likes** — лайки фильмов пользователями
7. **friendships** — **односторонние** связи дружбы пользователей

---

## REST-эндпоинты (основные)

### Genres
- `GET /genres` — список жанров
- `GET /genres/{id}` — жанр по id

### MPA
- `GET /mpa` — список рейтингов
- `GET /mpa/{id}` — рейтинг по id

### Films
- `GET /films` — список фильмов
- `GET /films/{id}` — фильм по id (включая `mpa` и `genres`, жанры — без дублей, отсортированы по `id`)
- `POST /films` — создание фильма
- `PUT /films` — обновление фильма
- `GET /films/popular?count={n}` — популярные фильмы
- `PUT /films/{filmId}/like/{userId}` — поставить лайк
- `DELETE /films/{filmId}/like/{userId}` — убрать лайк

### Users
- `GET /users`, `GET /users/{id}`, `POST /users`, `PUT /users`, `DELETE /users/{id}`
- `PUT /users/{id}/friends/{friendId}` — добавить друга (**односторонне**)
- `DELETE /users/{id}/friends/{friendId}` — удалить из друзей
- `GET /users/{id}/friends` — список друзей
- `GET /users/{id}/friends/common/{otherId}` — общие друзья

---

## Структура репозитория

- `src/main/resources/schema.sql` — создание таблиц (`IF NOT EXISTS`)
- `src/main/resources/data.sql` — начальные данные (жанры, MPA) через `MERGE ... KEY(id)`
- `src/main/resources/application.properties` — конфигурация H2 и профиля `jdbc`
- `src/main/java/.../infrastructure/jdbc` — JDBC-мапперы и репозитории
- `src/main/java/.../service` — бизнес-логика
- `src/main/java/.../web` — контроллеры, DTO, мапперы, обработчик ошибок

---

## Примеры SQL-запросов

### 1) Все жанры фильма по ID
```sql
SELECT g.name
FROM genres g
JOIN film_genres fg ON g.id = fg.genre_id
WHERE fg.film_id = 1;
```

### 2) Топ-5 фильмов по количеству лайков
```sql
SELECT f.id, f.name, COUNT(fl.user_id) AS likes_count
FROM films f
LEFT JOIN film_likes fl ON f.id = fl.film_id
GROUP BY f.id, f.name
ORDER BY likes_count DESC
LIMIT 5;
```

### 3) Добавить друга (односторонняя дружба)
```sql
INSERT INTO friendships (user_id, friend_id)
VALUES (1, 2);
```

### 4) Общие друзья двух пользователей
```sql
SELECT u.id, u.name
FROM users u
JOIN friendships f1 ON u.id = f1.friend_id AND f1.user_id = 1
JOIN friendships f2 ON u.id = f2.friend_id AND f2.user_id = 2;
```

---

## Запуск
1. Собрать и запустить Spring Boot-приложение (`mvn spring-boot:run` или из IDE).
2. При старте автоматически выполняются `schema.sql` и `data.sql`.
3. Проверить справочники:
    - `GET http://localhost:8080/genres`
    - `GET http://localhost:8080/mpa`
4. Открыть H2 Console: `http://localhost:8080/h2-console` → ввести JDBC URL `jdbc:h2:file:./db/filmorate`.
