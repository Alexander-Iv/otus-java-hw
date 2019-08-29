package alexander.ivanov.ms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String getObjectNodeAsString(String name, String value) {
        return getObjectNodeAsString(Collections.singletonMap(name, value));
    }

    public static String getObjectNodeAsString(Map<String, Object> objects) {
        logger.info("JsonHelper.getObjectNodeAsString");
        ObjectNode objectNode = mapper.createObjectNode();
        objects.forEach((s, o) -> {
            logger.info("adding s = {}, o = {}", s, o);
            objectNode.setAll(mapper.createObjectNode().putPOJO(s, o));
        });

        logger.info("objectNode.toString() = {}", objectNode.toString());
        return objectNode.toString();
    }
}
