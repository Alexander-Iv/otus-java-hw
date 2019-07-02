package alexander.ivanov.hibernate.dao;

import alexander.ivanov.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.hibernate.model.Address;
import alexander.ivanov.hibernate.model.Phone;
import alexander.ivanov.hibernate.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

    @Test
    void creatingUserTest() {
        UserDao userDao = new UserDaoImpl();

        User newUser = new User("NoName",
                0,
                new Address("Lenin street"),
                Arrays.asList(new Phone("02"), new Phone("03")));

        userDao.create(newUser);

        assertEquals(new User(1L,"NoName", 0, new Address(1L,"Lenin street"),
                Arrays.asList(new Phone(1L,"02"), new Phone(2L,"03"))),
                userDao.load(1L, User.class));
    }

    @Test
    void updatingUserTest() {
        UserDao userDao = new UserDaoImpl();

        User newUser = new User("Qwerty",
                123,
                new Address("empty"),
                Arrays.asList(new Phone("00000"), new Phone("00000")));

        userDao.create(newUser);

        newUser.setPhone(Arrays.asList(new Phone(1L,"11111"), new Phone(2L,"22222")));
        newUser.setAddress(new Address(1L,"djdjdj"));
        newUser.setAge(20);

        userDao.update(newUser);

        assertEquals(new User(1L,"Qwerty",
                        20,
                        new Address(1L,"djdjdj"),
                        Arrays.asList(new Phone(1L,"11111"), new Phone(2L,"22222"))),
                userDao.load(1L, User.class));
    }
}