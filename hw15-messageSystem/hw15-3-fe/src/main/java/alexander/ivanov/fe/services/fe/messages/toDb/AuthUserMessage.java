package alexander.ivanov.fe.services.fe.messages.toDb;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.Receiver;
import alexander.ivanov.messageSystem.services.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUserMessage implements Message {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserMessage.class);

    private final MessageSystemContext context;
    private final String userName;
    private final String userPassword;

    public AuthUserMessage(MessageSystemContext context, String userName, String userPassword) {
        this.context = context;
        this.userName = userName;
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
        logger.debug("AuthUserMessage.process");
        DbService dbService = (DbService)receiver;
        dbService.auth(userName, userPassword);
        logger.debug("dbService = {}", dbService);
    }
}
