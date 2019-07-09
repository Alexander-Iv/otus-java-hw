package alexander.ivanov.webserver.util;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Loader {
    private static final Logger logger = LoggerFactory.getLogger(Loader.class);
    //private static final String PATH_TO_SERVLET_PKG = "alexander.ivanov.webserver.servlets";
    //private static final String PATH_TO_SERVLET_DIR = Loader.class.getResource("../servlets/").getPath();
    private static ServletContextHandler context;

    public Loader(ServletContextHandler context) {
        this.context = context;
    }

    public void executeOnDir(String path, Command command) {
        String dirPath = null;
        try {
            dirPath = Loader.class.getResource("../" + path).getPath();
        } catch (Exception e) {
            logger.warn(command.getClass().getSimpleName().replace("Add", "") + " by path \"" + path + "\" not found!");
        }
        if (dirPath == null || dirPath.isEmpty()) {
            return;
        }
        logger.debug("dirPath = {}", dirPath);

        String packagePath = "alexander.ivanov.webserver." + path.replace("/", ".").replace("\\", ".");
        logger.debug("PATH_TO_SERVLET_PKG = {}", packagePath);
        //logger.debug("PATH_TO_SERVLET_DIR = {}", PATH_TO_SERVLET_DIR);

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
                    clazz = Loader.class.getClassLoader().loadClass(packagePath + className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null) {
                    String servletPath = "/" + clazz.getSimpleName().toLowerCase();
                    //context.AddServlet(servlet, servletPath);
                    command.execute(clazz, path);
                    logger.info("added servletPath = {}, servletClass = {}", servletPath, clazz);
                }
            }
        });
    }

    private interface Command {
        void execute(Class clazz, String path);
    }

    public static class AddServlet implements Command {
        @Override
        public void execute(Class clazz, String path) {
            context.addServlet(clazz, path);
        }
    }

    public static class AddFilter implements Command {
        @Override
        public void execute(Class clazz, String path) {
            context.addFilter(clazz, path, null);
        }
    }
}
