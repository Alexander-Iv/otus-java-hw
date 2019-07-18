package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

public class Resources {
    private static final Logger logger = LoggerFactory.getLogger(Resources.class);
    private static Class applicationClass = Application.class;

    public static URL getResourcePathUrl(String name) {
        URL resourceUrl = applicationClass.getResource(name);
        return resourceUrl;
    }

    public static String getResourcePath(String name) {
        return getResourcePathUrl(name).toString();
    }

    public static InputStream getResourceAsStream(String name) {
        return applicationClass.getResourceAsStream(name);
    }
}
