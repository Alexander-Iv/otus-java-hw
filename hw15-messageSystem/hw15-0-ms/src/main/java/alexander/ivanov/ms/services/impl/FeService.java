package alexander.ivanov.ms.services.impl;

import alexander.ivanov.ms.Message;
import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.util.JsonHelper;
import alexander.ivanov.ms.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class FeService implements MessageClient {
    private static final Logger logger = LoggerFactory.getLogger(FeService.class);
    private final MessageSystem ms;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public FeService(MessageSystem ms, SimpMessagingTemplate simpMessagingTemplate) {
        this.ms = ms;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void init() {
        this.ms.addClient(this);
    }

    @Override
    public void accept(Message msg) {
        logger.info("FeService.accept");
        logger.info("msg = {}", msg);
        /*try {
            //modelling some work
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if (msg.process().contains("UserNotFound")) {
            //https://www.baeldung.com/spring-websockets-send-message-to-user
            simpMessagingTemplate.convertAndSend("/message-broker", JsonHelper.getObjectNodeAsString("result","User Not found. Please registers."));
        } else if (msg.process().contains("created")) {
            logger.info("msg.process() = {}", msg.process());
            if (msg.process().contains("withSession")) {
                String users = MessageHelper.getJsonFieldValueByName(msg.process(), "Users");
                simpMessagingTemplate.convertAndSend("/message-broker", msg.process());
            } else {
                simpMessagingTemplate.convertAndSend("/message-broker", JsonHelper.getObjectNodeAsString("redirect", "/auth/login"));
            }
        } else if (msg.process().contains("Users")) {
            String authValue = MessageHelper.getJsonFieldValueByName(msg.process(), "auth");
            logger.info("authValue = {}", authValue);

            simpMessagingTemplate.convertAndSend("/message-broker", msg.process());
        } else if (msg.process().contains("logout")) {
            simpMessagingTemplate.convertAndSend("/message-broker", JsonHelper.getObjectNodeAsString("redirect", "/auth/logout"));
        }
        logger.info("FeService.end");
    }
}
