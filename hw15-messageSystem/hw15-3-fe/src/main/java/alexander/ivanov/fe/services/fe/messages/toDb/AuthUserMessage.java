package alexander.ivanov.fe.services.fe.messages.toDb;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.Receiver;
import alexander.ivanov.messageSystem.services.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class AuthUserMessage implements Message {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserMessage.class);

    private MessageSystemContext context;
    private String userName;
    private String userPassword;

    public AuthUserMessage(MessageSystemContext context) {
        this.context = context;
    }

    //@Autowired
    private void setContext(MessageSystemContext context) {
        logger.info("AuthUserMessage.setContext");
        logger.info("1 AuthUserMessage.context = {}", context);
        this.context = context;
        logger.info("2 AuthUserMessage.context = {}", context);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public Address getFrom() {
        logger.info("AuthUserMessage.getFrom");
        logger.info("context.getFeAddress() = {}", context.getFeAddress());
        return context.getFeAddress();
    }

    @Override
    public Address getTo() {
        logger.info("AuthUserMessage.getTo");
        logger.info("context.getDbAddress() = {}", context.getDbAddress());
        return context.getDbAddress();
    }

    @Override
    public void process(Receiver receiver) {
        logger.debug("AuthUserMessage.process");
        DbService dbService = (DbService)receiver;
        dbService.auth(userName, userPassword);
        logger.debug("dbService = {}", dbService);
    }
}
