package alexander.ivanov.ms.util;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonStringEncoder jsonStringEncoder = new JsonStringEncoder();

    public static String getObjectNodeAsString(String name, String value) {
        return getObjectNodeAsString(Collections.singletonMap(name, value));
    }

    public static String getObjectNodeAsString(Map<String, Object> objects) {
        logger.info("JsonHelper.getObjectNodeAsString");
        ObjectNode objectNode = mapper.createObjectNode();
        objects.forEach((s, o) -> {
            logger.info("adding s = {}, o = {}, t = {}", s, o, o.getClass().getTypeName());
            ObjectNode newObjectNode = mapper.createObjectNode();
            if (o instanceof List<?>) {
                logger.info("o instanceof Collection");
                ArrayNode arrayNode = mapper.createArrayNode();
                ((List<?>)o).forEach(arrayNode::addPOJO);
                newObjectNode.putArray(s).addAll(arrayNode);
            } else {
                newObjectNode.putPOJO(s, mapper.valueToTree(o));
            }
            objectNode.setAll(newObjectNode);
        });

        logger.info("objectNode.toString() = {}", objectNode.toString());
        logger.info("objectNode.textValue() = {}", objectNode.textValue());
        logger.info("objectNode.asText() = {}", objectNode.asText());

        return objectNode.toString();
    }
}
