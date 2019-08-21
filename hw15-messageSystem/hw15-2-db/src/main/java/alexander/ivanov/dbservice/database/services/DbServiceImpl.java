package alexander.ivanov.dbservice.database.services;

import alexander.ivanov.dbservice.database.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.dbservice.database.services.messages.toFe.IsCorrectUser;
import alexander.ivanov.dbservice.database.services.messages.toFe.IsIncorrectUser;
import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.MessageSystem;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.services.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DbServiceImpl extends UserDaoImpl implements DbService {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

    private final MessageSystemContext context;

    @Autowired
    public DbServiceImpl(MessageSystemContext context) {
        this.context = context;
    }

    @Override
    public void init() {
        logger.debug("DbServiceImpl.init");
        context.getMessageSystem().addReceiver(this);
        logger.debug("this = {}", this);
    }

    @Override
    public Address getAddress() {
        return context.getDbAddress();
    }

    @Override
    public MessageSystem getMessageSystem() {
        return context.getMessageSystem();
    }

    @Override
    public void add(String name, String password) {
        create(new User(name, password));
    }

    @Override
    public void auth(String name, String password) {
        logger.debug("DbServiceImpl.auth");
        Collection<User> users = loadAll();
        User newUser = new User(name, password);
        if (users.contains(newUser)) {
            logger.debug("isCorrectUser");
            context.getMessageSystem().sendMessage(new IsCorrectUser(context));
        } else {
            context.getMessageSystem().sendMessage(new IsIncorrectUser(context));
            logger.debug("isIncorrectUser");
        }
    }

    @Override
    public Collection<User> findAll() {
        return loadAll();
    }

    @Override
    public String toString() {
        return "DbServiceImpl{" +
                "context=" + context +
                '}';
    }
}
