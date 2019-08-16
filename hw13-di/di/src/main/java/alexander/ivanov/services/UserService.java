package alexander.ivanov.services;

import alexander.ivanov.dbservice.database.hibernate.model.User;

import java.util.Collection;

public interface UserService {
    void add(User user);
    Collection<User> findAll();
}
