package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.config.MapperConfig;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("jdbc")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.sql.init.mode=always",
        "spring.datasource.url=jdbc:h2:mem:filmorate_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@Import({MapperConfig.class, UserJdbcRepository.class})
class UserJdbcRepositoryIT {

    @Autowired
    private UserJdbcRepository users;

    private User newUser(String email, String login) {
        User u = new User();
        u.setEmail(email);
        u.setLogin(login);
        u.setName("Name");
        u.setBirthday(LocalDate.of(1990,1,1));
        return u;
    }

    @Test
    void createUpdateFind() {
        User saved = users.save(newUser("a@a.com","a"));
        Optional<User> found = users.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("a@a.com", found.get().getEmail());

        saved.setName("NewName");
        User updated = users.save(saved);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void friendsFlow() {
        User u1 = users.save(newUser("u1@a.com","u1"));
        User u2 = users.save(newUser("u2@a.com","u2"));

        users.addFriend(u1.getId(), u2.getId());
        List<User> f = users.findFriends(u1.getId());
        assertEquals(1, f.size());
        assertEquals(u2.getId(), f.getFirst().getId());

        users.removeFriend(u1.getId(), u2.getId());
        List<User> f2 = users.findFriends(u1.getId());
        assertTrue(f2.isEmpty());
    }

    @Test
    void deleteByIdRemovesUser() {
        User u = users.save(newUser("del@a.com","del"));
        assertTrue(users.deleteById(u.getId()));
        assertTrue(users.findById(u.getId()).isEmpty());
    }
}
