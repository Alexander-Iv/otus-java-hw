package alexander.ivanov.ms.util;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHelper {
    private static final Logger logger = LoggerFactory.getLogger(MessageHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static User getUserFromJsonMessage(String message) {
        logger.info("UserHelper.getUserFromJsonMessage");
        try {
            JsonNode userNode = getRootNode(message).findValue("User");
            logger.info("userNode = \n{}", userNode.toPrettyString());
            String userName, userPassword;
            if (userNode != null && !userNode.isEmpty()) {
                if (userNode.toString().contains("name") && userNode.toString().contains("password")) {
                    userName = getRootNode(message).findValue("name").asText();
                    userPassword = getRootNode(message).findValue("password").asText();
                } else {
                    userName = getRootNode(message).findValue("userName").asText();
                    userPassword = getRootNode(message).findValue("userPassword").asText();
                }
                logger.info("userName = {}, userPassword = {}", userName, userPassword);
                return new User(userName, userPassword);
            }
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }
        return null;
    }

    public static String getJsonFieldValueByName(String message, String field) {
        return getRootNode(message).findValue(field).asText();
    }

    private static JsonNode getRootNode(String message) {
        logger.info("MessageHelper.getRootNode");
        JsonNode rootNode;
        try {
            rootNode = mapper.readValue(message, JsonNode.class);
            logger.info("rootNode = {}", rootNode);
        } catch (JsonProcessingException e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
            throw new RuntimeException("Incorrect root node!");
        }
        return rootNode;
    }
}
