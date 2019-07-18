package alexander.ivanov.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class RelativeFile {
    private static final Logger logger = LoggerFactory.getLogger(RelativeFile.class);
    private static final String ROOT_DIR_NAME = "webserver";
    
    public static InputStream getAsStream(String path) {
        InputStream ins = null;
        try {
            ins = RelativeDirectoryPath.class.getResourceAsStream(".." + File.separator
                    + ".." + File.separator
                    + ROOT_DIR_NAME + File.separator
                    + path);
        } catch (Exception e) {
            logger.error("e.getMessage() = " + e.getMessage());
            throw new RuntimeException("No data found");
        }
        return ins;
    }
}
