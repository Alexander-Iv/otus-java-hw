package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.impl.DaoImpl;
import alexander.ivanov.orm.object.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountJdbcTemplateTest {
    @Test
    void create() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default1"));
        userJdbcTemplate.create(new Account(0L, "qwerty", 11));
        assertEquals(new Account(0L, "qwerty", 11), userJdbcTemplate.load(0L, Account.class));
    }

    @Test
    void update() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default2"));
        userJdbcTemplate.create(new Account(0L, "qwerty", 11));
        userJdbcTemplate.update(new Account(0L, "ytrewq", 123));
        assertEquals(new Account(0L, "ytrewq", 123), userJdbcTemplate.load(0L, Account.class));
    }

    @Test
    void createOrUpdateIfNotExist() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default3"));
        userJdbcTemplate.createOrUpdate(new Account(0L, "qwerty", 11));
        assertEquals(new Account(0L, "qwerty", 11), userJdbcTemplate.load(0L, Account.class));
    }

    @Test
    void createOrUpdateIfExist() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default4"));
        userJdbcTemplate.create(new Account(0L, "qwerty", 11));
        userJdbcTemplate.createOrUpdate(new Account(0L, "ytrewq", 123));
        assertEquals(new Account(0L, "ytrewq", 123), userJdbcTemplate.load(0L, Account.class));
    }

    @Test
    void load() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DaoImpl("jdbc:h2:mem:default5"));
        userJdbcTemplate.create(new Account(0L, "qqqqqq", 11));
        userJdbcTemplate.create(new Account(1L, "wwwwww", 11));
        userJdbcTemplate.create(new Account(2L, "eeeeee", 11));
        userJdbcTemplate.load(2L, Account.class);
    }
}
