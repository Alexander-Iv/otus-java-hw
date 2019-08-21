package alexander.ivanov.fe.services.fe;

import alexander.ivanov.fe.services.fe.messages.toDb.AddUserMessage;
import alexander.ivanov.fe.services.fe.messages.toDb.AuthUserMessage;
import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.MessageSystem;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.services.FeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeServiceImpl implements FeService {
    private static final Logger logger = LoggerFactory.getLogger(FeServiceImpl.class);

    private MessageSystemContext context;

    @Autowired
    public FeServiceImpl(MessageSystemContext context) {
        logger.debug("FeServiceImpl.FeServiceImpl");
        this.context = context;
        logger.debug("context = {}", context);
    }

    @Override
    public void init() {
        logger.debug("FeServiceImpl.init");
        context.getMessageSystem().addReceiver(this);
        context.getMessageSystem().start();
        logger.debug("this = {}", this);
    }

    @Override
    public Address getAddress() {
        return context.getFeAddress();
    }

    @Override
    public MessageSystem getMessageSystem() {
        return context.getMessageSystem();
    }

    @Override
    public void auth(String name, String password) {
        AuthUserMessage message = new AuthUserMessage(context);
        message.setUserName(name);
        message.setUserPassword(password);
        init();
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void registration(String name, String password) {
        AddUserMessage message = new AddUserMessage();
        message.setUserName(name);
        message.setUserPassword(password);
        init();
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public String toString() {
        return "FeServiceImpl{" +
                "context=" + context +
                '}';
    }
}
