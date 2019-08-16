package alexander.ivanov.dbservice.database.hibernate.dao.impl;

import alexander.ivanov.cache.Cache;
import alexander.ivanov.cache.impl.CacheImpl;
import alexander.ivanov.dbservice.database.hibernate.config.impl.HibernateConfig;
import alexander.ivanov.dbservice.database.hibernate.dao.UserDao;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private SessionFactory sessionFactory;
    private Cache<Long, User> cache;

    public UserDaoImpl() {
        this(new HibernateConfig().configure(), new CacheImpl<>(10, 0, 0, true));
    }

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory, Cache<Long, User> cache) {
        this.sessionFactory = sessionFactory;
        this.cache = cache;
    }

    @Override
    public void create(User user) {
        executeTransaction(new CreateFunc(), user);
    }

    @Override
    public void update(User user) {
        executeTransaction(new UpdateFunc(), user);
    }


    @Override
    public User load(long id, Class<User> clazz) {
        User object = cache.get(id);
        if (Objects.nonNull(object)) {
            logger.info("object from cache = {}", object);
            return object;
        }

        object = executeTransaction(new LoadFunc(), new IdClass(id, clazz));
        cache.put(id, object);
        return object;
    }

    private <T, R> R executeTransaction(BiFunction<Session, T, R> func, T object) {
        try(Session session = sessionFactory.openSession()) {
            R obj = null;
            try {
                session.beginTransaction();
                obj = func.apply(session, object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                logger.error("e.getMessage() = {}", e.getMessage());
            }
            return obj;
        }
    }

    private class UpdateFunc implements BiFunction<Session, User, User> {
        @Override
        public User apply(Session session, User user) {
            session.update(user);
            cache.put(user.getId(), user);
            return null;
        }
    }

    private class CreateFunc implements BiFunction<Session, User, User> {
        @Override
        public User apply(Session session, User user) {
            session.save(user);
            cache.put(user.getId(), user);
            return null;
        }
    }

    private class LoadFunc implements BiFunction<Session, IdClass, User> {
        @Override
        public User apply(Session session, IdClass idClass) {
            return (User)session.get(idClass.getClazz(), idClass.getId());
        }
    }

    private class IdClass {
        private Long id;
        private Class<?> clazz;

        IdClass(Long id, Class<?> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        Long getId() {
            return id;
        }

        Class<?> getClazz() {
            return clazz;
        }
    }

    @Override
    public List<User> loadAll() {
        List<User> users;

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            try {
                users = session.createQuery(
                        "select u from User u", User.class)
                        .getResultList();
            } catch (NoResultException e) {
                session.getTransaction().rollback();
                throw new NoResultException("No Data found");
            }

            session.getTransaction().commit();
        }
        return users;
    }
}
