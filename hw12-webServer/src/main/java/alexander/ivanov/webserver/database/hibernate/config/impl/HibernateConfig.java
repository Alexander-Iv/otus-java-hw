package alexander.ivanov.webserver.database.hibernate.config.impl;

import alexander.ivanov.webserver.database.hibernate.config.Config;
import alexander.ivanov.webserver.database.hibernate.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateConfig implements Config {
    private static final String CONFIG_FILE_PATH = "alexander/ivanov/webserver/database/hibernate/config/hibernate.cfg.xml";

    @Override
    public SessionFactory configure() {
        Configuration configuration = new Configuration().configure(CONFIG_FILE_PATH);

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }
}
