package alexander.ivanov.fe.util;

import alexander.ivanov.fe.model.User;
import alexander.ivanov.fe.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String readAuthNameFromJson(String jsonAsString) {
        try {
            return mapper.readValue(jsonAsString, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Users readUsersFromJson(String jsonAsString) {
        try {
            User[] users = mapper.readValue(jsonAsString, User[].class);
            return new Users(Arrays.asList(users));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
