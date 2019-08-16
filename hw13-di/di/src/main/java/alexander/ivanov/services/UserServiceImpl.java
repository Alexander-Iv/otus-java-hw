package alexander.ivanov.services;

import alexander.ivanov.dbservice.database.hibernate.dao.UserDao;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add(User user) {
        userDao.create(user);
    }

    @Override
    public Collection<User> findAll() {
        return userDao.loadAll();
    }
}
