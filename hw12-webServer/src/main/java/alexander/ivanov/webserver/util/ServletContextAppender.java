package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.util.appender.Appender;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ServletContextAppender {
    private static final Logger logger = LoggerFactory.getLogger(ServletContextAppender.class);
    private static final String ROOT_PACKAGE_PATH = "alexander.ivanov.webserver";
    private static ServletContextHandler context;

    public ServletContextAppender(ServletContextHandler context) {
        this.context = context;
    }

    public void appendFrom(String path, Appender appender) {
        String dirPath = RelativeDirectoryPath.get(path);
        if (dirPath.isEmpty()) {
            return;
        }

        String packagePath = ROOT_PACKAGE_PATH + "."
                + path.replace("/", ".").replace("\\", ".");
        logger.debug("PATH_TO_SERVLET_PKG = {}", packagePath);

        List<File> files = Arrays.asList(new File(dirPath).listFiles());
        files.forEach(file -> {
            if (file.isFile()) {
                logger.debug("{}, {}", file.getName(), file.getPath());
                Class clazz = null;
                String className = null;
                try {
                    logger.debug("file.getPath() = {}", file.getPath());
                    className = file.getName().replace(".class", "");
                    logger.debug("className = {}", className);
                    clazz = ServletContextAppender.class.getClassLoader().loadClass(packagePath + className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null) {
                    String servletPath = "/" + clazz.getSimpleName().toLowerCase();
                    //context.Servlet(servlet, servletPath);
                    appender.append(clazz, servletPath);
                    logger.info("added servletPath = {}, servletClass = {}", servletPath, clazz);
                }
            }
        });
    }

    public static class Servlet implements Appender<Class> {
        @Override
        public void append(Class clazz, String path) {
            context.addServlet(clazz, path);
        }
    }

    public static class Filter implements Appender<Class> {
        @Override
        public void append(Class clazz, String path) {
            context.addFilter(clazz, path, null);
        }
    }
}
