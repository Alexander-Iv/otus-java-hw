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

            User newUser = new User("unknown",
                    22,
                    new Address("qwerty1"),
                    Arrays.asList(new Phone("1111111111"), new Phone("2222222222")));
            session.save(newUser);

            User newUser2 = new User("unknown",
                    44,
                    new Address("qwerty2"),
                    Arrays.asList(new Phone("3333333333"), new Phone("44444444444")));
            session.save(newUser2);

            session.getTransaction().commit();

            logger.info("user1 = {}", newUser);
            logger.info("user2 = {}", newUser2);

            User loadedUser = session.get(User.class, 2L);
            logger.info("loadedUser.getId() = {}", loadedUser.getId());
            logger.info("loadedUser = {}", loadedUser);
        }
    }
}
