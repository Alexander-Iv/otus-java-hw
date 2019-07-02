package alexander.ivanov.hibernate.dao.impl;

import alexander.ivanov.hibernate.dao.UserDao;
import alexander.ivanov.hibernate.data.Config;
import alexander.ivanov.hibernate.data.impl.HibernateConfig;
import alexander.ivanov.hibernate.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDaoImpl implements UserDao {
    private SessionFactory sessionFactory;

    public UserDaoImpl() {
        Config config = new HibernateConfig();
        this.sessionFactory = config.configure();
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
}
