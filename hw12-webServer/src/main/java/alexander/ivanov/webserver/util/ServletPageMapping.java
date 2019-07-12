package alexander.ivanov.webserver.util;

import org.eclipse.jetty.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletPageMapping {
    private static final Logger logger = LoggerFactory.getLogger(ServletPageMapping.class);
    private static Map<String, String> map = new HashMap<>();

    static {
        RelativeFilesPath.get("servlets" + File.separator);
        RelativeFilesPath.get("webapp" + File.separator);

        Collection keys =
        RelativeFilesPath.getNames().stream()
                .map(s -> s.toLowerCase()
                        .replace(".class", "")
                        //.replace(".html", "")
                        //.replace(File.separator, "/")
                )
                .collect(Collectors.toSet())
        ;

        logger.info("keys = {}", keys);
    }

    public static void mapping(String servletContext) {
    }
}
