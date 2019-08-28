package alexander.ivanov.ms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultConverter {
    private static final Logger logger = LoggerFactory.getLogger(ResultConverter.class);

    public static String toJson(String result) {
        logger.info("ResultConverter.toJson");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("result", result);
        logger.info("objectNode.toString() = {}", objectNode.toString());
        return objectNode.toString();
    }
}
