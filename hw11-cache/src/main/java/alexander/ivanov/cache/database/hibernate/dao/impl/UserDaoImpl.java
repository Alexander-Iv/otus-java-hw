package alexander.ivanov.cache.database.hibernate.dao.impl;

import alexander.ivanov.cache.cache.Cache;
import alexander.ivanov.cache.cache.impl.CacheImpl;
import alexander.ivanov.cache.database.hibernate.config.impl.HibernateConfig;
import alexander.ivanov.cache.database.hibernate.dao.UserDao;
import alexander.ivanov.cache.database.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private SessionFactory sessionFactory;
    private Cache<Long, User> cache;

    public UserDaoImpl() {
        this(new HibernateConfig().configure(), new CacheImpl(10, 0, 0, true));
    }

    public UserDaoImpl(SessionFactory sessionFactory, Cache cache) {
        this.sessionFactory = sessionFactory;
        this.cache = cache;
    }

    @Override
    public void create(User objectData) {
        executeTransaction(new CreateFunc(), objectData);
    }

    @Override
    public void update(User objectData) {
        executeTransaction(new UpdateFunc(), objectData);
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        T object = (T)cache.get(id);
        if (Objects.nonNull(object)) {
            logger.info("object from cache = {}", object);
            return object;
        }

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            object = session.get(clazz, id);
            session.getTransaction().commit();
            cache.put(id, (User)object);
        }
        return object;
    }

    private void executeTransaction(BiFunction func, Object object) {
        try(Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                func.apply(session, object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                logger.error("e.getMessage() = {}", e.getMessage());
            }
        }
    }

    private class UpdateFunc implements BiFunction<Session, Object, Void> {
        @Override
        public Void apply(Session session, Object o) {
            session.update(o);
            return null;
        }
    }

    private class CreateFunc implements BiFunction<Session, Object, Void> {
        @Override
        public Void apply(Session session, Object o) {
            session.save(o);
            return null;
        }
    }

    public List<User> loadAll() {
        List<User> users = null;

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
