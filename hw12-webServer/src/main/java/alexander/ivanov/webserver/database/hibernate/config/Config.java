package alexander.ivanov.webserver.database.hibernate.config;

import org.hibernate.SessionFactory;

public interface Config {
    SessionFactory configure();
}
