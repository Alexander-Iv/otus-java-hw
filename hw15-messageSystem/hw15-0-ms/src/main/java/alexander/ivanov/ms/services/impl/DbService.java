package alexander.ivanov.ms.services.impl;

import alexander.ivanov.dbservice.database.hibernate.dao.UserDao;
import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.ms.Message;
import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.util.JsonHelper;
import alexander.ivanov.ms.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        /*try {
            //modelling some work
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        User userFromJson = MessageHelper.getUserFromJsonMessage(msg.process());
        logger.info("userFromJson = {}", userFromJson);

        Map<String, Object> allParams = new LinkedHashMap<>();
        Optional<User> loadedUser;
        if (msg.process().contains("create")) {
            logger.info("creating user");
            userDao.create(userFromJson);
            loadedUser = userDao.load(userFromJson.getName(), userFromJson.getPassword());
            loadedUser.ifPresent(user -> {
                if (msg.process().contains("withSession")) {
                    allParams.put("param1", "withSession");
                    allParams.put("param2", "created");
                    allParams.put("User", user);
                    allParams.put("Users", userDao.loadAll());
                    sendMessageToFe(JsonHelper.getObjectNodeAsString(allParams));
                } else {
                    sendMessageToFe("created:" + user);
                }
            });
            return;
        }

        loadedUser = userDao.load(userFromJson.getName(), userFromJson.getPassword());
        loadedUser.ifPresentOrElse(user -> {
            logger.info("loadedUser.get() = {}", user);
            List<User> users = userDao.loadAll();
            allParams.put("auth", user.getName());
            allParams.put("Users", users);

            sendMessageToFe(JsonHelper.getObjectNodeAsString(allParams));
        }, () -> {
            logger.info("user {} not found", userFromJson);
            sendMessageToFe("UserNotFound");
        });
        logger.info("DbService.end");
    }

    private void sendMessageToFe(String data) {
        logger.info("DbService.sendMessageToFe");
        logger.info("data = {}", data);
        ms.sendMessage(ms.createMessageFor("FeService", data));
    }
}
