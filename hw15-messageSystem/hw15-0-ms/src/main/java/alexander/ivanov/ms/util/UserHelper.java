package alexander.ivanov.ms.util;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserHelper {
    public static User getUserFromJsonMessage(String message) {
        JsonNode jsonNode;
        String userName = null, userPassword = null;
        try {
            jsonNode = new ObjectMapper().readValue(message, JsonNode.class);
            userName = jsonNode.get("userName").asText();
            userPassword = jsonNode.get("userPassword").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new User(userName, userPassword);
    }
}
