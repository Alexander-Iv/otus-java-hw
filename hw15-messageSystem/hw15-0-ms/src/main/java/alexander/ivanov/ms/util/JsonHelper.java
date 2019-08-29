package alexander.ivanov.ms.util;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.util.RawValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonStringEncoder jsonStringEncoder = new JsonStringEncoder();

    public static String getObjectNodeAsString(String name, String value) {
        return getObjectNodeAsString(Collections.singletonMap(name, value));
    }

    //FIXME
    public static String getObjectNodeAsString(Map<String, Object> objects) {
        logger.info("JsonHelper.getObjectNodeAsString");
        ObjectNode objectNode = mapper.createObjectNode();
        StringBuffer tmp = new StringBuffer();
        objects.forEach((s, o) -> {
            logger.info("adding s = {}, o = {}", s, o);
            ObjectNode newObjectNode = mapper.createObjectNode();
            if (o instanceof String) {
                newObjectNode.put(s, getEncodedString((String)o));
            } else {
                //newObjectNode.putRawValue(s, new RawValue(getEncodedString(o.toString())));
                newObjectNode.putPOJO(s, o);
                //tmp.append("\"").append(s).append("\"").append(":").append(o);
            }
            logger.info("newObjectNode.toString() = " + newObjectNode.toString());
            logger.info("newObjectNode.textValue() = " + newObjectNode.textValue());
            logger.info("newObjectNode.asText() = " + newObjectNode.asText());

            objectNode.setAll(newObjectNode);
        });

        logger.info("objectNode.toString() = {}", objectNode.toString());
        logger.info("objectNode.textValue() = {}", objectNode.textValue());
        logger.info("objectNode.asText() = {}", objectNode.asText());

        return getEncodedString(objectNode.toString()).replace("\\", "");
    }

    private static String getEncodedString(String quoteString) {
        return new String(jsonStringEncoder.encodeAsUTF8(quoteString));
    }
}
