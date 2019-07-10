package alexander.ivanov.webserver.util.appender;

import alexander.ivanov.webserver.util.RelativeDirectoryPath;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
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
                //+ path.replace("/", ".").replace("\\", ".");
                + appender.getClass().getSimpleName().toLowerCase() + "s";
        logger.debug("PATH_TO_SERVLET_PKG = {}", packagePath);

        List<File> files = Arrays.asList(new File(dirPath).listFiles());
        append(dirPath, files, packagePath, appender);
        /*files.forEach(file -> {
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
            } else {
                logger.info("file = {}", file.getName());
            }
        });*/
    }

    private void append(String dirPath, List<File> files, String packagePath, Appender appender) {
        files.forEach(file -> {
            if (file.isFile()) {
                logger.debug("{}, {}", file.getName(), file.getPath());
                String appenderDirName = appender.getClass().getSimpleName().toLowerCase() + "s";
                Class clazz = null;
                String className = null;
                try {
                    logger.debug("appenderDirName = {}", appenderDirName);
                    logger.debug("dirPath.indexOf(appenderDirName) = {}", dirPath.indexOf(appenderDirName));
                    String subPackage = dirPath.substring(dirPath.indexOf(appenderDirName))
                            .replace(File.separator, ".");
                    logger.debug("subPackage = {}", subPackage);

                    logger.debug("file.getPath() = {}", file.getPath());
                    className = file.getName().replace(".class", "");
                    logger.debug("className = {}, packagePath = {}", className, packagePath);
                    String fullClassName = packagePath.replace(appenderDirName, subPackage) + "." + className;
                    logger.debug("classFullName = {}", fullClassName);
                    clazz = ServletContextAppender.class.getClassLoader().loadClass(fullClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null && clazz.getSuperclass().equals(HttpServlet.class)) {
                    String fullLowerClassName = clazz.getName().toLowerCase();
                    String servletPath = fullLowerClassName
                            .substring(fullLowerClassName.indexOf(appenderDirName) + appenderDirName.length())
                            .replace(".", "/");
                    //context.Servlet(servlet, servletPath);
                    appender.append(clazz, servletPath);
                    logger.debug("added servletPath = {}, servletClass = {}", servletPath, clazz);
                }
            } else {
                String newDirPath = dirPath + File.separator + file.getName() /*+ File.separator*/;
                logger.info("newDirPath = {}", newDirPath);
                append(newDirPath, Arrays.asList(new File(newDirPath).listFiles()), packagePath, appender);
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
