package alexander.ivanov.webserver;

import alexander.ivanov.webserver.web.impl.WebServerImpl;
import alexander.ivanov.webserver.web.WebServer;

public class Application {
    public static void main(String[] args) {
        WebServer server = new WebServerImpl();
        server.start();
    }
}
