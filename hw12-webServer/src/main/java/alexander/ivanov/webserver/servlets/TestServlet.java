package alexander.ivanov.webserver.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resultAsString = "<p>Test Page</p>";
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
