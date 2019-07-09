package alexander.ivanov.webserver.impl;

import alexander.ivanov.webserver.WebServer;
import alexander.ivanov.webserver.util.JettyServerUtil;
import alexander.ivanov.webserver.util.ServletContextAppender;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collections;

public class WebServerImpl implements WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServerImpl.class);
    private static final int SERVER_PORT = 8080;
    private Server server;

    @Override
    public void start() {
        try {
            server = JettyServerUtil.createServer(SERVER_PORT);
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
}
