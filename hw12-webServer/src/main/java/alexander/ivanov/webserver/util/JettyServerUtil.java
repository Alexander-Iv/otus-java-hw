package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.models.hibernate.data.Config;
import alexander.ivanov.webserver.models.hibernate.data.impl.HibernateConfig;
import alexander.ivanov.webserver.util.appender.ServletContextAppender;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JettyServerUtil {
    private static final Logger logger = LoggerFactory.getLogger(JettyServerUtil.class);

    public static Server createServer(int port) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/*");

        ServletContextAppender appender = new ServletContextAppender(context);
        appender.appendFrom("servlets/", new ServletContextAppender.Servlet());
        appender.appendFrom("filters/", new ServletContextAppender.Filter());

        Config hibernateConfig = new HibernateConfig();
        SessionFactory sessionFactory = hibernateConfig.configure();
        context.setAttribute("sessionFactory", sessionFactory);

        Server server = new Server(port);

        List<ContextHandler> handlers = new ArrayList<>();
        handlers.addAll(createResourceContexts());
        handlers.add(context);

        logger.info("handlers = " + handlers);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(handlers.toArray(new Handler[]{}));
        logger.info("contexts = " + contexts);

        server.setHandler(contexts);
        return server;
    }

    static List<ContextHandler> createResourceContexts() {
        List<ContextHandler> contexts = new ArrayList<>();
        RelativeFilesPath.get("webapp/").stream()
                .filter(s -> s.contains(".html"))
                .forEach(s -> {
                    logger.info("s = " + s);

                    ResourceHandler resourceHandler = new ResourceHandler();
                    String resourcePath = RelativeDirectoryPath.get("webapp/").replace("webapp", s);
                    logger.info("resourcePath = " + resourcePath);
                    resourceHandler.setResourceBase(resourcePath);

                    String contextPath = s.replace("webapp","")
                            .replace(File.separator, "/")
                            .replace(".html", "") + "/*";

                    if (contextPath.contains("index")) {
                        contextPath = "/*";
                    }
                    logger.info("contextPath = " + contextPath);

                    ContextHandler contextHandler = new ContextHandler(contextPath);
                    contextHandler.setHandler(resourceHandler);

                    contexts.add(contextHandler);
        });

        logger.info("contexts = " + contexts);
        return contexts;
    }
}
