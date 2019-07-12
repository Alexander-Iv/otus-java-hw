package alexander.ivanov.webserver.util;

import org.eclipse.jetty.util.URIUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RelativeFilesPathTest {
    private static final Logger logger = LoggerFactory.getLogger(RelativeFilesPathTest.class);

    @Test
    void test() {
        RelativeFilesPath.get("servlets" + File.separator).forEach(s -> {
            logger.info("s = {}", s);
        });
        logger.info("");
        RelativeFilesPath.get("webapp" + File.separator).forEach(s -> {
            logger.info("s = {}", s);
        });

        logger.info("URIUtil.encodePath(\"/qwerty\") = {}", URIUtil.encodePath("/qwerty/"));
    }
}