package alexander.ivanov.cache.database.hibernate.config;

import org.hibernate.SessionFactory;

public interface Config {
    SessionFactory configure();
}
