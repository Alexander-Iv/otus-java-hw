package alexander.ivanov.webserver.models.hibernate.data;

import org.hibernate.SessionFactory;

public interface Config {
    SessionFactory configure();
}
