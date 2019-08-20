package alexander.ivanov.messageSystem.services;

import java.util.Collection;

public interface DbService extends Service {
    void add(String name, String password);
    void auth(String name, String password);
    Collection<?> findAll();
}
