package alexander.ivanov.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResourceContextMapper {
    private static final Logger logger = LoggerFactory.getLogger(ResourceContextMapper.class);

    public static Map<String, URL> getContextPathAndUrl(String directory) {
        Map<String, URL> contextWithUri = new HashMap<>();
        Resources.getResourceFilesUri(directory).forEach(uri -> {
            try {
                contextWithUri.put(
                        uriToContext(uri.toString(), directory),
                        Resources.getResourcePathUrl(uriToFileName(uri.toString(), directory))
                );
            } catch (Resources.ResourceNotFoundException e) {
                e.printStackTrace();
            }
        });

        return contextWithUri;
    }

    private static String uriToContext(String uri, String directory) {
        return //uri.substring(uri.indexOf(directory))
                uriToFileName(uri, directory)
                .replace(directory, "")
                .replace(".html", "/*")
                .replace("index/*", "*");
    }

    private static String uriToFileName(String uri, String directory) {
        return uri.substring(uri.indexOf(directory));
    }
}
