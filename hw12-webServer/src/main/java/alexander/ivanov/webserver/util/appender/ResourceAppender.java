package alexander.ivanov.webserver.util.appender;

import alexander.ivanov.webserver.util.RelativeDirectoryPath;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceAppender {
    private static final Logger logger = LoggerFactory.getLogger(ResourceAppender.class);
    private static ResourceHandler resourceHandler;

    public ResourceAppender(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    public void appendFrom(String path, Appender appender) {
        String dirPath = RelativeDirectoryPath.get(path);
        if (dirPath.isEmpty()) {
            return;
        }
        appender.append(resourceHandler, dirPath);
    }

    public static class Welcome implements Appender<ResourceHandler> {
        @Override
        public void append(ResourceHandler resourceHandler, String path) {
            List<File> files = Arrays.asList(new File(path).listFiles());
            logger.info("files = " + files);
            List<String> fileNames = files.stream()
                    .filter(file -> file.isFile())
                    .filter(file -> file.getName().contains(".html"))
                    .filter(file -> file.getName().contains("index"))
                    .map(file -> file.getName()).collect(Collectors.toList());
            logger.info("fileNames = " + fileNames);
            resourceHandler.setWelcomeFiles(fileNames.toArray(new String[fileNames.size()]));
            logger.info("resourceHandler.getWelcomeFiles() = " + resourceHandler.getWelcomeFiles());
        }
    }

    public static class Base implements Appender<ResourceHandler> {
        @Override
        public void append(ResourceHandler resourceHandler, String path) {
            resourceHandler.setResourceBase(path + "/login.html");
            logger.info("resourceHandler.getResourceBase() = " + resourceHandler.getResourceBase());
        }
    }
}
