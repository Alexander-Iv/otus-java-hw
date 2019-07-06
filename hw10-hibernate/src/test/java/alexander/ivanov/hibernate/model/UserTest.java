package alexander.ivanov.hibernate.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    private static SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
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

    @Test
    void cascadeCreating() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User newUser = new User("unknown",
                    22,
                    new Address("qwerty1"),
                    Arrays.asList(new Phone("1111111111"), new Phone("2222222222")));

            session.save(newUser);
            session.getTransaction().commit();

            logger.info("session.get(User.class, 1L) = " + session.get(User.class, 1L));
            logger.info("session.get(Address.class, 1L) = " + session.get(Address.class, 1L));
            logger.info("session.get(Phone.class, 1L) = " + session.get(Phone.class, 1L));
            logger.info("session.get(Phone.class, 2L) = " + session.get(Phone.class, 2L));

            assertEquals(new Address(1L, "qwerty1"), session.get(Address.class, 1L));
            assertEquals(new Phone(1L, "1111111111"), session.get(Phone.class, 1L));
            assertEquals(new Phone(2L, "2222222222"), session.get(Phone.class, 2L));
            assertEquals(new User(1L,"unknown", 22, new Address(1L,"qwerty1"),
                    Arrays.asList(new Phone(1L,"1111111111"), new Phone(2L,"2222222222"))),
                    session.get(User.class, 1L));
        }
    }

    @Test
    void cascadeDeleting() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User newUser = new User("incognito",
                    44,
                    new Address("empty"),
                    Arrays.asList(new Phone("1234567890"), new Phone("0987654321")));

            session.save(newUser);
            session.getTransaction().commit();

            logger.info("session.get(User.class, 1L) = " + session.get(User.class, 1L));
            logger.info("session.get(Address.class, 1L) = " + session.get(Address.class, 1L));
            logger.info("session.get(Phone.class, 1L) = " + session.get(Phone.class, 1L));
            logger.info("session.get(Phone.class, 2L) = " + session.get(Phone.class, 2L));

            session.beginTransaction();
            assertNotNull(session.get(User.class, 1l));

            session.delete(newUser);
            session.getTransaction().commit();

            assertNull(session.get(Address.class, 1L));
            assertNull(session.get(Phone.class, 1L));
            assertNull(session.get(Phone.class, 2L));
            assertNull(session.get(User.class, 1L));

        }
    }
}