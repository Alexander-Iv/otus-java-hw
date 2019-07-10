package alexander.ivanov.webserver.servlets;

import alexander.ivanov.webserver.util.RelativeDirectoryPath;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class Login extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        //resp.setContentType("text/html");

        PrintWriter printWriter = resp.getWriter();
        printWriter.println("context path = " + getServletContext().getContextPath());
        printWriter.println("servlet name = " + getServletName());
        printWriter.println("servlet info = " + getServletInfo());
        while(getServletConfig().getInitParameterNames().hasMoreElements()) {
            String parameterName = getServletConfig().getInitParameterNames().nextElement();
            printWriter.print("parameter " + parameterName + " value = " + getServletConfig().getInitParameter(parameterName));
        }
        printWriter.println("servlet path = " + req.getServletPath());
        printWriter.println("servlet dispatcherType = " + req.getDispatcherType());
        printWriter.println("servlet requestDispatcher = " + req.getRequestDispatcher("/login.html"));
        printWriter.println("servlet requestDispatcher = " + req.getRequestDispatcher("alexander/ivanov/webserver/webapp/login.html"));
        printWriter.println("servlet URI = " + req.getRequestURI());
        printWriter.println("servlet URL = " + req.getRequestURL());
        //req.getRequestDispatcher("login.html").forward(req, resp);
        printWriter.println("servlet dispatcher = " + getServletContext().getRequestDispatcher("/login.html"));
        //getServletContext().getRequestDispatcher("/login.html").forward(req, resp);
        printWriter.println("path to file = " + new File(RelativeDirectoryPath.get("webapp/")+"/index.html").toURI().toURL().getPath());
        printWriter.println("servlet contextPath = " + getServletContext().getContextPath());
        printWriter.println("servlet contextPath = " + Paths.get(RelativeDirectoryPath.get("webapp/")).toAbsolutePath());
        printWriter.println("servlet context = " + getServletContext().getContext("/login.html").getResource("/login.html"));
        //printWriter.println("servlet resource = " + getServletContext().getResource(new File(RelativeDirectoryPath.get("webapp/")+"/index.html").toURI().toURL().getPath()));
        printWriter.println("servlet resource = " + getServletContext().getResource("/"));
        printWriter.println("servlet resource = " + Resource.newResource(RelativeDirectoryPath.get("webapp/")+"/index.html"));
        //Resource.newResource(RelativeDirectoryPath.get("webapp/")+"/index.html")
        printWriter.println("servlet resource = " + getServletContext().getNamedDispatcher("login"));
        printWriter.println("servlet resource = " + getServletContext().getNamedDispatcher("/login"));
        printWriter.println("servlet resource = " + getServletContext().getNamedDispatcher("/login.html"));
        printWriter.println("RequestDispatcher FORWARD_PATH_INFO = " + RequestDispatcher.FORWARD_PATH_INFO);
        printWriter.println("RequestDispatcher INCLUDE_PATH_INFO = " + RequestDispatcher.INCLUDE_PATH_INFO);

        printWriter.println("~~~ = " + req.getContextPath() + req.getServletPath());

        //resp.sendRedirect("/login.html");

        //printWriter.println("~~~ = " + ServletContextHandler.getCurrentContext().getRequestDispatcher("/login.html"));
        //ServletContextHandler.getCurrentContext().getRequestDispatcher("/login").include(req, resp);

        getServletContext().getRequestDispatcher("/login.html")
                //.include(req, resp);
                //.forward(req, resp);
        ;

        RequestDispatcher dispatcher = //getServletContext().getRequestDispatcher("/login.html");
                getServletContext().getNamedDispatcher("/login.html");
        printWriter.println("dispatcher = " + dispatcher);
        //printWriter.flush();

        //dispatcher.include(req, resp);
        //dispatcher.forward(req, resp);

        printWriter.println("~~~ = " + " " + String.format("%s.html", req.getServletPath()));
        (dispatcher = req.getServletContext()
                .getRequestDispatcher(String.format("%s.html", req.getServletPath()))).include(req, resp);

        dispatcher.forward(req, resp);
    }

}
