package alexander.ivanov.ms.services.impl;

import alexander.ivanov.ms.Message;
import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.util.ResultConverter;
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
        try {
            //modelling some work
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (msg.process().contains("UserNotFound")) {
            //https://www.baeldung.com/spring-websockets-send-message-to-user
            simpMessagingTemplate.convertAndSend("/message-broker", ResultConverter.toJson("User Not found. Please registers."));
        }

        logger.info("FeService.end");
    }
}
