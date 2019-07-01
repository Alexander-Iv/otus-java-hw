package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.impl.DaoImpl;
import alexander.ivanov.orm.object.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserJdbcTemplateTest {

    @Test
    void create() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default6"));
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
        assertEquals(new User(0L, "qwerty", 11), userJdbcTemplate.load(0L, User.class));
    }

    @Test
    void update() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default7"));
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
        userJdbcTemplate.update(new User(0L, "ytrewq", 88));
        assertEquals(new User(0L, "ytrewq", 88), userJdbcTemplate.load(0L, User.class));
    }

    @Test
    void createOrUpdateIfNotExist() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default8"));
        userJdbcTemplate.createOrUpdate(new User(0L, "qwerty", 11));
        assertEquals(new User(0L, "qwerty", 11), userJdbcTemplate.load(0L, User.class));
    }

    @Test
    void createOrUpdateIfExist() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default9"));
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
        userJdbcTemplate.createOrUpdate(new User(0L, "ytrewq", 123));
        assertEquals(new User(0L, "ytrewq", 123), userJdbcTemplate.load(0L, User.class));
    }

    @Test
    void load() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default10"));
        //userJdbcTemplate.create(new User(555L, "aaa", 11));
        //userJdbcTemplate.createOrUpdate(new User(333L, "bbb", 11));
        userJdbcTemplate.createOrUpdate(new User(123L, "ccc", 11));
        assertEquals(new User(123L, "ccc", 11), userJdbcTemplate.load(123L, User.class));
        //userJdbcTemplate.load(0, User.class); //error no data found
    }
}