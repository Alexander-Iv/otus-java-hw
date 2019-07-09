package alexander.ivanov.webserver.util;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class JettyServerUtil {
    private static final Logger logger = LoggerFactory.getLogger(JettyServerUtil.class);

    public static Server createServer(int port) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //context.addServlet(DefaultServlet.class, "/*");
        ServletContextAppender appender = new ServletContextAppender(context);
        appender.appendFrom("servlets/", new ServletContextAppender.Servlet());
        appender.appendFrom("filters/", new ServletContextAppender.Filter());
        Server server = new Server(port);
        server.setHandler(new HandlerList(context));
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});
        server.setHandler(handlers);
        return server;
    }
    static ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        ResourceAppender appender = new ResourceAppender(resourceHandler);
        appender.appendFrom("pages/welcome/", new ResourceAppender.Welcome());
        appender.appendFrom("pages/", new ResourceAppender.Base());
        return resourceHandler;
    }

    static SecurityHandler createSecurityHandler(ServletContextHandler context) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(false);
        //constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        //security.setAuthenticator(new BasicAuthenticator());

        /*URL propFile = Appl.class.getClassLoader().getResource("realm.properties");
        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }

        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));*/
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }
}
