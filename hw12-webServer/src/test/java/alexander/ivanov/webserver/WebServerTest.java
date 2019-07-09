package alexander.ivanov.webserver;

import alexander.ivanov.webserver.impl.WebServerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebServerTest {
    @Test
    void test() {
        WebServer server = new WebServerImpl();
        server.start();
    }
}