package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.util.appender.ResourceAppender;
import alexander.ivanov.webserver.util.appender.ServletContextAppender;
import org.eclipse.jetty.http.HttpContent;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.ResourceHttpContent;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.URLResource;
import org.eclipse.jetty.util.security.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;

public class JettyServerUtil {
    private static final Logger logger = LoggerFactory.getLogger(JettyServerUtil.class);

    public static Server createServer(int port) {
        HandlerList handlerList = new HandlerList();

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(new ServletHolder("hello", HelloServlet.class), "/hello");
        //logger.info("servletHandler.getServlets() = {}", servletHandler.getServlets());
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
        //context.setContextPath("/");
        //context.addServlet(HelloServlet.class, "/hello");
        ServletContextAppender appender = new ServletContextAppender(context);
        appender.appendFrom("servlets/", new ServletContextAppender.Servlet());
        appender.appendFrom("filters/", new ServletContextAppender.Filter());

        //ResourceHandler rh0 = new ResourceHandler();
        ResourceHandler rh0 = new ResourceHandler();
        rh0.setResourceBase(RelativeDirectoryPath.get("webapp/") + "/login.html");
        rh0.setDirectoriesListed(true);
        logger.info("Resource.newResource(file0) = " + RelativeDirectoryPath.get("webapp/") + "/login.html");

        ContextHandler context0 = new ContextHandler("/login");
        //context0.setResourceBase(RelativeDirectoryPath.get("webapp/") + "/login.html");
        context0.setHandler(rh0);
        //context0.setHandler(rhc);
        logger.info("context0 = " + context0);

        Server server = new Server(port);
        //server.setHandler(new HandlerCollection(createResourceHandler(context), /*context0,*/ context));

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context0, context });

        server.setHandler(contexts);

        //HandlerList handlers = new HandlerList();
        //handlers.setHandlers(new Handler[]{createResourceHandler(context), createSecurityHandler(context)});
        //server.setHandler(handlers);
        //server.dumpStdErr();

        return server;
    }
    static ResourceHandler createResourceHandler(ServletContextHandler context) {
        //new URLResource("","");
        //new ResourceService()
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        ResourceAppender appender = new ResourceAppender(resourceHandler);
        //appender.appendFrom("webapp/welcome/", new ResourceAppender.Welcome());w
        appender.appendFrom("webapp/", new ResourceAppender.Base());
        //logger.info("resourceHandler.getResourceBase() = {}", resourceHandler.getResourceBase());
        //logger.info("resourceHandler.getResource(\"/index.html\") = {}", resourceHandler.getResource("/login.html"));
        context.setResourceBase(resourceHandler.getResourceBase());
        return resourceHandler;
    }

    static SecurityHandler createSecurityHandler(ServletContextHandler context) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(false);
        constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        /*URL propFile = Appl.class.getClassLoader().getResource("realm.properties");
        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }*/

        String pathPropertyFile = RelativeDirectoryPath.get("property/") + File.separator +"realm.properties";
        logger.info("pathPropertyFile = {}", pathPropertyFile);
        security.setLoginService(new HashLoginService("MyRealm", pathPropertyFile));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }

    public static class HelloServlet extends HttpServlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print("<h3>Hello from HelloServlet</h3>");
            response.getWriter().print("<p>" + getServletContext().getContext("/") + "</p>");
            response.getWriter().print("<p>" + getServletContext().getContext("/login.html") + "</p>");
            response.getWriter().print("<p>" + getServletContext().getContext("/login.html").getContextPath() + "</p>");
            //response.getWriter().print("<p>" + getServletContext().getContext("/login.html").getResource("file:///D:/IdeaProjects/otus-java-hw/hw12-webServer/target/classes/alexander/ivanov/webserver/webapp/login.html") + "</p>");
            response.getWriter().print("<p>" + getServletContext().getContext("/login.html").getRequestDispatcher("/login.html") + "</p>");
            response.getWriter().print("<p>" + getServletContext().getContext("/login.html").getNamedDispatcher("/login.html") + "</p>");
            //response.getWriter().println("<p>" + getServletContext().getContext("/").getNamedDispatcher("/login.html") + "</p>");
            response.getWriter().print("<p>" + getServletContext().getRequestDispatcher("/login.html") + "</p>");

            response.getWriter().print("<p>" + request.getRequestDispatcher("/login.html") + "</p>");

            //RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("/login.html");
            //RequestDispatcher dispatcher = ContextHandler.getCurrentContext().getRequestDispatcher("/login.html");
            //RequestDispatcher dispatcher = getServletContext().getContext("/login.html").getRequestDispatcher("/login.html");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login");
            response.getWriter().print("<p>dispatcher = </p> <p>" + dispatcher + "</p>");

            ServletContext ctx = getServletContext()
                    .getContext("/login.html");
            response.getWriter().print("ctx = " + ctx + "<br>");
            response.getWriter().print("ctx = " + ctx.getServletContextName() + "<br>");
            response.getWriter().print("ctx = " + ctx.getRequestDispatcher("/login.html") + "<br>");
            response.getWriter().print("ctx = " + ctx.getResourcePaths("/login.html") + "<br>");
            response.getWriter().print("ctx = " + request.getRequestDispatcher("/login.html") + "<br>");

            response.getWriter().print("httpURI = "
                    + ContextHandler.getCurrentContext().getRequestDispatcher("/login.html")
                    + "<br>");


            dispatcher.include(request, response);
            dispatcher.forward(request, response);
            /*getServletContext()
                    .getContext("/login.html")
                    .getRequestDispatcher("/login.html")
                    .include(request, response);*/
            /*getServletContext()
                    .getContext("/login.html")
                    .getRequestDispatcher("/login.html")
                    .include(request, response);
            getServletContext()
                    .getContext("/login.html")
                    .getRequestDispatcher("/login.html")
                    .forward(request, response);*/
        }
    }
}
