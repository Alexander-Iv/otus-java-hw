package alexander.ivanov.hibernate;

import alexander.ivanov.hibernate.model.Address;
import alexander.ivanov.hibernate.model.Phone;
import alexander.ivanov.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);
    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        Demo demo = new Demo();

        demo.example1();
    }

    private Demo() {
        Configuration configuration = new Configuration().configure("alexander/ivanov/hibernate/config/hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Phone.class)
                .addAnnotatedClass(Address.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private void example1() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User newUser = new User("unknown", 0, new Address(), Arrays.asList(new Phone(), new Phone()));
            session.save(newUser);
            session.getTransaction().commit();

            logger.info("user = {}", newUser);

            User loadedUser = session.load(User.class, -1L);
            logger.info("loadedUser.getId() = {}", loadedUser.getId());
            logger.info("loadedUser = {}", loadedUser);
        }

    }
}
