package alexander.ivanov.ms.util;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

public class MessageHelper {
    private static final Logger logger = LoggerFactory.getLogger(MessageHelper.class);

    public static User getUserFromJsonMessage(String message) {
        logger.info("UserHelper.getUserFromJsonMessage");
        try {
            //JsonNode userNode = getNodeByName(message,"User");
            JsonNode userNode = getRootNode(message).findValue("User");
            logger.info("userNode = \n{}", userNode.toPrettyString());
            String userName, userPassword;
            if (userNode != null && !userNode.isEmpty()) {
                userName = getRootNode(message).findValue("userName").asText();
                userPassword = getRootNode(message).findValue("userPassword").asText();

                logger.info("userName = {}, userPassword = {}", userName, userPassword);
                return new User(userName, userPassword);
            }
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }
        return null;
    }

    private static JsonNode getNodeByName(String message, String name) {
        try {
            logger.info("getRootNode(message).findParent(name).asText() = {}", getRootNode(message).get(name).asText());
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        try {
            logger.info("getRootNode(message).findParent(name).asText() = {}", getRootNode(message).findParent(name).asText());
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        try {
            logger.info("getRootNode(message).findPath(name).asText() = {}", getRootNode(message).findPath(name).asText());
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        try {
            logger.info("getRootNode(message).findValue(name).asText() = {}", getRootNode(message).findValue(name).asText());
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        Iterator<JsonNode> iter;
        try {
            logger.info("elements:");
            iter = getRootNode(message).elements();
            while (iter.hasNext()) {
                JsonNode node = iter.next();
                logger.info("node = {}", node);
            }
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        try {
            logger.info("iterator:");
            iter = getRootNode(message).iterator();
            while (iter.hasNext()) {
                JsonNode node = iter.next();
                logger.info("node = {}", node);
            }
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        try {
            logger.info("fieldNames:");
            Iterator<String> fnIter = getRootNode(message).fieldNames();
            while (fnIter.hasNext()) {
                String node = fnIter.next();
                logger.info("node = {}", node);
            }
        } catch (Exception e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
        }

        logger.info("fieldNames:");
        Iterator<Map.Entry<String, JsonNode>> iterator = getRootNode(message).fields();
        while(iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            logger.info("entry.key = {}, entry.value = {}", entry.getKey(), entry.getValue());
            if (entry.getKey().equals(name)) {
                return (JsonNode)entry.getValue();
            }
        }

        return null;
    }

    private static JsonNode getRootNode(String message) {
        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readValue(message, JsonNode.class);
        } catch (JsonProcessingException e) {
            ErrorHandlerHelper.printErrorStackTrace(e.getStackTrace());
            throw new RuntimeException("Incorrect root node!");
        }
        return rootNode;
    }
}
