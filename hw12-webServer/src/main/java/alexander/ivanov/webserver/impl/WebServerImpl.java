package alexander.ivanov.webserver.impl;

import alexander.ivanov.webserver.WebServer;
import alexander.ivanov.webserver.models.hibernate.data.Config;
import alexander.ivanov.webserver.models.hibernate.data.impl.HibernateConfig;
import alexander.ivanov.webserver.util.RelativeFilesPath;
import alexander.ivanov.webserver.util.ResourceContextMapper;
import alexander.ivanov.webserver.util.Resources;
import alexander.ivanov.webserver.util.appender.ServletContextAppender;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebServerImpl implements WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServerImpl.class);
    private static final int SERVER_PORT = 9090;
    private Server server;

    @Override
    public void start() {
        try {
            server = createServer(SERVER_PORT);
            server.setStopTimeout(5*60*1000);
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("e.getMessage() = {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.error("e.getMessage() = {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private Server createServer(int port) {
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

    private List<ContextHandler> createResourceContexts() {
        List<ContextHandler> contexts = new ArrayList<>();

        ResourceContextMapper.getContextPathAndUrl("webapp/").forEach((s, url) -> {

            logger.info("Resource.newClassPathResource(s) = {}", Resource.newClassPathResource(s));

            logger.info("contextPath = {}", s);
            logger.info("url = {}", url.toString());
            logger.info("url.toExternalForm() = {}", url.toExternalForm());
            logger.info("url.getPath() = {}", url.getPath());
            logger.info("url = {}", url.getFile());
            logger.info("url.toExternalForm() = {}", url.toExternalForm());
            logger.info("Resource.newResource(url) = {}", Resource.newResource(url));

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setBaseResource(Resource.newResource(url));

            ContextHandler contextHandler = new ContextHandler(s);
            contextHandler.setHandler(resourceHandler);

            contexts.add(contextHandler);
        });

        /*RelativeFilesPath.get("webapp/").stream()
                .filter(s -> s.contains(".html"))
                .forEach(s -> {
                    logger.info("s = " + s);

                    ResourceHandler resourceHandler = new ResourceHandler();
                    String resourcePath = Resources.getResourcePath(s);
                    logger.info("resourcePath = " + resourcePath);
                    resourceHandler.setResourceBase(resourcePath);

                    String contextPath = s.substring(s.lastIndexOf(File.separator))
                            .replace(File.separator, "/")
                            .replace(".html", "/*");

                    if (contextPath.contains("index")) {
                        contextPath = "/*";
                    }
                    logger.info("contextPath = " + contextPath);

                    ContextHandler contextHandler = new ContextHandler(contextPath);
                    contextHandler.setHandler(resourceHandler);

                    contexts.add(contextHandler);
                });*/

        //logger.info("contexts = " + contexts);
        contexts.forEach(contextHandler -> {
            logger.info("context = {}", contextHandler);
        });
        return contexts;
    }
}
