package alexander.ivanov.hibernate.data;

import org.hibernate.SessionFactory;

public interface Config {
    SessionFactory configure();
}
