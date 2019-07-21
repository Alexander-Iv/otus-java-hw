package alexander.ivanov.webserver.web.impl;

import alexander.ivanov.webserver.web.WebServer;
import alexander.ivanov.webserver.web.filters.IndexFilter;
import alexander.ivanov.webserver.database.hibernate.config.Config;
import alexander.ivanov.webserver.database.hibernate.config.impl.HibernateConfig;
import alexander.ivanov.webserver.web.servlets.AuthServlet;
import alexander.ivanov.webserver.web.servlets.HomeServlet;
import alexander.ivanov.webserver.web.servlets.LogoutServlet;
import alexander.ivanov.webserver.web.servlets.RegisterServlet;
import alexander.ivanov.webserver.web.util.Resources;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.util.log.Log;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class WebServerImpl implements WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServerImpl.class);
    private static final int SERVER_PORT = 9090;
    private Server server;

    @Override
    public void start() {
        try {
            Log.getLog().setDebugEnabled(true);
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
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(new ServletHolder("hello", HelloServlet.class), "/hello");
        Arrays.asList(servletHandler.getServlets()).forEach(servletHolder -> {
            logger.info("servletHolder.getName() = {}", servletHolder.getName());
        });

        ServletContextHandler context = new ServletContextHandler(
                null,
                "/",
                new SessionHandler(),
                new ConstraintSecurityHandler(),
                servletHandler,
                new ErrorPageErrorHandler()
        );

        Config hibernateConfig = new HibernateConfig();
        SessionFactory sessionFactory = hibernateConfig.configure();

        context.addFilter(new FilterHolder(new IndexFilter()), "/*", null);

        context.addServlet(new ServletHolder("Auth", new AuthServlet(sessionFactory)), "/auth/*");
        context.addServlet(new ServletHolder("Home", new HomeServlet(sessionFactory)), "/home/*");
        context.addServlet(new ServletHolder("Logout", new LogoutServlet()), "/logout/*");
        context.addServlet(new ServletHolder("Register", new RegisterServlet(sessionFactory)), "/register/*");

        Server server = new Server(port);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{createResourceContexts(context), context});

        server.setHandler(/*contexts*/handlers);
        return server;
    }

    private ResourceHandler createResourceContexts(HandlerContainer parent) {
        logger.info("WebServerImpl.createResourceContexts");
        String rootDir = "web/pages";

        ResourceHandler rootResourceHandler = new ResourceHandler();
        String path = Resources.getResourcePath(rootDir);
        logger.info("path  = " + path);
        rootResourceHandler.setResourceBase(path);
        return rootResourceHandler;
    }

    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.getWriter().print("<h3>Hello from HelloServlet</h3>");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
