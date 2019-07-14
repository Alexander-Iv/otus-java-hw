package alexander.ivanov.webserver.impl;

import alexander.ivanov.webserver.WebServer;
import alexander.ivanov.webserver.util.JettyServerUtil;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerImpl implements WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServerImpl.class);
    private static final int SERVER_PORT = 9090;
    private Server server;

    @Override
    public void start() {
        try {
            server = JettyServerUtil.createServer(SERVER_PORT);
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
}
