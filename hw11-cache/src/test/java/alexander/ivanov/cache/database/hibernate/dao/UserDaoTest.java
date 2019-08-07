package alexander.ivanov.cache.database.hibernate.dao;

import alexander.ivanov.cache.database.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.cache.database.hibernate.model.Address;
import alexander.ivanov.cache.database.hibernate.model.Phone;
import alexander.ivanov.cache.database.hibernate.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    @Test
    void checkUserWithCache() {
        UserDao userDao = new UserDaoImpl();

        User newUser = new User("NoName",
                0,
                new Address("Lenin street"),
                Arrays.asList(new Phone("02"), new Phone("03")));

        userDao.create(newUser);

        assertEquals(new User(1L,"NoName", 0, new Address(1L,"Lenin street"),
                        Arrays.asList(new Phone(1L,"02"), new Phone(2L,"03"))),
                userDao.load(1L, User.class));

        //повтороно должно быть из кэша - проверяем сообщение: object from cache =
        userDao.load(1L, User.class);
    }
}