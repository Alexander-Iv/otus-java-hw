package alexander.ivanov.ms.controllers;

import alexander.ivanov.ms.MessageSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    //private final FeService feService;
    private final MessageSystem messageSystem;
    //private final MessageClient feService;

    @Autowired
    public MessageController(MessageSystem messageSystem) {
        //this.feService = feService;
        this.messageSystem = messageSystem;
        //this.dbService = dbService;
    }

    @MessageMapping("/login/message")
    @SendTo("/topic/response")
    public String loginMessageHandler(String message) {
        logger.info("got login message: {}", message);
        String userName = null;
        String userPassword = null;
        try {
            JsonNode jsonNode = new ObjectMapper().readValue(message, JsonNode.class);
            userName = jsonNode.get("userName").asText();
            userPassword = jsonNode.get("userPassword").asText();
            logger.debug("userName = {}", userName);
            logger.debug("userPassword = {}", userPassword);
            //messageSystem.createMessageFor(dbService, message);
            messageSystem.sendMessage(messageSystem.createMessageFor("DbService", message));
            //feService.auth(userName, userPassword);
        } catch (Exception e) {
            List<?> errorStack = Arrays.stream(e.getStackTrace()).map(stackTraceElement -> stackTraceElement.toString() + "\n").collect(Collectors.toList());
            logger.error("ERROR STACKTRACE:\n{}", errorStack);
            return String.format("{\"result\":\"%s\"}", "Incorrect user or password. Please try again.");
        }
        return String.format("{\"result\":\"%s\"}", "");
    }
}
