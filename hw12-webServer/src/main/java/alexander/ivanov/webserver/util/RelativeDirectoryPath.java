package alexander.ivanov.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RelativeDirectoryPath {
    private static final Logger logger = LoggerFactory.getLogger(RelativeDirectoryPath.class);
    private static final String ROOT_DIR_NAME = "webserver";
    private static String NO_DIRECTORY_FOUND = "Relative directory path(\"$PATH$\") not found!";

    public static String get(String path) {
        String dirPath = "";
        try {
            File dir = new File(RelativeDirectoryPath.class.getResource(".." + File.separator
                    + ".." + File.separator
                    + ROOT_DIR_NAME + File.separator
                    + path).toURI());

            if (dir.exists()) {
                dirPath = dir.getPath();
            }
        } catch (Exception e) {
            logger.error("e.getMessage() = " + e.getMessage());
            logger.warn(NO_DIRECTORY_FOUND.replace("$PATH$", path));
        }
        logger.debug("dirPath = {}", dirPath);
        return dirPath;
    }

    /*public static String getRootPackagePath() {
        return ROOT_PACKAGE_PATH;
    }*/
}
