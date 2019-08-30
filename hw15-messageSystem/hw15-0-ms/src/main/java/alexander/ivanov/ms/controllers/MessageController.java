package alexander.ivanov.ms.controllers;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.util.ErrorHandlerHelper;
import alexander.ivanov.ms.util.JsonHelper;
import alexander.ivanov.ms.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageSystem messageSystem;

    @Autowired
    public MessageController(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @SendTo("/message-broker")
    public String toBroker(String message) {
        return message;
    }

    @MessageMapping("/login/message")
    @SendTo("/message-broker")
    public String loginMessageHandler(String message) {
        return sendMessageAndReturnResult("DbService", message);
    }

    @MessageMapping("/register/message")
    @SendTo("/message-broker")
    public String registerMessageHandler(String message) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("command", "create");
        User newUser = MessageHelper.getUserFromJsonMessage(message);
        params.put("User", newUser);
        return sendMessageAndReturnResult("DbService",JsonHelper.getObjectNodeAsString(params));
    }

    private String sendMessageAndReturnResult(String targetClient, String message) {
        logger.info("MessageController.sendMessageAndReturnResult");
        logger.info("targetClient = {}, message = {}", targetClient, message);
        try {
            messageSystem.sendMessage(messageSystem.createMessageFor(targetClient, message));
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
            return JsonHelper.getObjectNodeAsString("result", "Incorrect user or password. Please try again.");
        }
        return JsonHelper.getObjectNodeAsString("result", "");
    }
}
