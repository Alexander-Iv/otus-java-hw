package alexander.ivanov.dbservice.database.hibernate.dao.impl;

import alexander.ivanov.cache.Cache;
import alexander.ivanov.cache.impl.CacheImpl;
import alexander.ivanov.dbservice.database.hibernate.config.impl.HibernateConfig;
import alexander.ivanov.dbservice.database.hibernate.dao.UserDao;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.BiFunction;

@Repository
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

    @Override
    public Optional<User> load(String name, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("NAME", name);
        params.put("PASSWORD", password);
        List<User> users = executeTransaction(new ExecuteUserQueryWithParamsFunc(), params);
        logger.info("users loaded from DB:");
        users.forEach(user -> {
            logger.info("user = {}", user);
        });
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return users.stream().findFirst();
    }

    @Override
    public List<User> loadAll() {
        List<User> users;
        users = executeTransaction(new ExecuteUserQueryWithParamsFunc(), Collections.emptyMap());
        users.forEach(user -> {
            cache.put(user.getId(), user);
        });
        return users;
    }

    private <T, R> R executeTransaction(BiFunction<Session, T, R> func, T object) {
        try (Session session = sessionFactory.openSession()) {
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
            return (User) session.get(idClass.getClazz(), idClass.getId());
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

    private class ExecuteUserQueryWithParamsFunc implements BiFunction<Session, Map<String, Object>, List<User>> {
        private final String SELECT_USER = "select u from User u";
        private StringBuffer conditions = new StringBuffer();

        @Override
        public List<User> apply(Session session, Map<String, Object> params) {
            logger.info("ExecuteUserQueryWithParamsFunc.apply");
            initConditions(params);
            Query<User> query = session.createQuery(SELECT_USER + conditions, User.class);
            logger.info("query.toString() = {}", query.toString());
            params.forEach(query::setParameter);
            return query.getResultList();
        }

        private void initConditions(Map<String, Object> params) {
            logger.info("ExecuteUserQueryWithParamsFunc.initConditions");
            if (!params.isEmpty()) {
                conditions.append(" WHERE ");
            }
            Iterator<String> iter = params.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                conditions.append(key).append(" = ").append(":").append(key);
                if (iter.hasNext()) {
                    conditions.append(" AND ");
                }
            }
            logger.info("conditions = {}", conditions);
        }
    }
}
