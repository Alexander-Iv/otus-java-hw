package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.util.appender.ServletContextAppender;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;

class ServletContextAppenderTest {
    @Test
    void loadTest() {
        new ServletContextAppender(new ServletContextHandler()).appendFrom("servlets/", new ServletContextAppender.Servlet());
        new ServletContextAppender(new ServletContextHandler()).appendFrom("filters/", new ServletContextAppender.Filter());
    }
}