package alexander.ivanov.webserver.models.hibernate.dao.impl;

import alexander.ivanov.webserver.models.hibernate.dao.UserDao;
import alexander.ivanov.webserver.models.hibernate.data.impl.HibernateConfig;
import alexander.ivanov.webserver.models.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private SessionFactory sessionFactory;

    public UserDaoImpl() {
        this(new HibernateConfig().configure());
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User objectData) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User objectData) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        T object = null;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            object = session.get(clazz, id);
            session.getTransaction().commit();
        }
        return object;
    }

    @Override
    public User load(String name, String password) {
        User object = null;

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            try {
                object = session.createQuery(
                        "select u from User u where u.name = :name and u.password = :password", User.class)
                        .setParameter( "name", name)
                        .setParameter( "password", password)
                        .getSingleResult();
            } catch (NoResultException e) {
                session.getTransaction().rollback();
                throw new NoResultException("No Data found");
            }

            session.getTransaction().commit();
        }
        return object;
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
