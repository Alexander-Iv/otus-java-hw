package alexander.ivanov.ms.services.impl;

import alexander.ivanov.dbservice.database.hibernate.dao.UserDao;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.ms.Message;
import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.util.ResultConverter;
import alexander.ivanov.ms.util.UserHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DbService implements MessageClient {
    private static final Logger logger = LoggerFactory.getLogger(DbService.class);
    private final MessageSystem ms;
    private final UserDao userDao;

    public DbService(MessageSystem ms, UserDao userDao) {
        this.ms = ms;
        this.userDao = userDao;
    }

    @Override
    public void init() {
        this.ms.addClient(this);
    }

    @Override
    public void accept(Message msg) {
        logger.info("DbService.accept");
        logger.info("msg = {}", msg);
        try {
            //modelling some work
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User userFromJson = UserHelper.getUserFromJsonMessage(msg.process());
        logger.info("userFromJson = {}", userFromJson);
        List<User> users = userDao.loadAll();
        logger.info("users = {}", users);
        if (users.contains(userFromJson)) {
            logger.info("user {} exists", userFromJson);
        } else {
            logger.info("user {} not found", userFromJson);
            ms.sendMessage(ms.createMessageFor("FeService", ResultConverter.toJson("UserNotFound")));
        }
        logger.info("DbService.end");
    }
}
