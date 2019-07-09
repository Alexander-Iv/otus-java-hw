package alexander.ivanov.webserver.util;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;

class LoaderTest {
    @Test
    void loadTest() {
        new Loader(new ServletContextHandler()).executeOnDir("servlets/", new Loader.AddServlet());
        new Loader(new ServletContextHandler()).executeOnDir("filters/", new Loader.AddFilter());
    }
}