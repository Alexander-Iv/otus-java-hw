package alexander.ivanov.fe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/login/message")
    @SendTo("/topic/response")
    public String loginMessageHandler(String message) {
        logger.info("got login message: {}", message);
        return String.format("{\"result\":\"%s\"}", "Is correct user");
    }
}
