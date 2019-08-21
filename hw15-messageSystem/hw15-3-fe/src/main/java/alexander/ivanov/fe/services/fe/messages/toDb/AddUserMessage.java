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

@Component
public class AddUserMessage implements Message {
    private static final Logger logger = LoggerFactory.getLogger(AddUserMessage.class);

    private MessageSystemContext context;
    private String userName;
    private String userPassword;

    @Autowired
    private void setContext(MessageSystemContext context) {
        this.context = context;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public Address getFrom() {
        return context.getFeAddress();
    }

    @Override
    public Address getTo() {
        return context.getDbAddress();
    }

    @Override
    public void process(Receiver receiver) {
        logger.debug("AddUserMessage.process");
        DbService dbService = (DbService)receiver;
        dbService.add(userName, userPassword);
        logger.debug("dbService = {}", dbService);
    }
}