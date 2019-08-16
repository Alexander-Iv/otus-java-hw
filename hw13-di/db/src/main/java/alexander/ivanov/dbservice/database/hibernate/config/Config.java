package alexander.ivanov.dbservice.database.hibernate.config;

import org.hibernate.SessionFactory;

public interface Config {
    SessionFactory configure();
}
