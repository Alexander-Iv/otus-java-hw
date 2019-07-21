package alexander.ivanov.webserver.web.servlets;

import alexander.ivanov.webserver.database.hibernate.dao.UserDao;
import alexander.ivanov.webserver.database.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.webserver.database.hibernate.model.User;
import alexander.ivanov.webserver.web.util.RelativeFileReader;
import alexander.ivanov.webserver.web.util.Resources;
import alexander.ivanov.webserver.web.util.WriterTemplate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    private static final String filePath = "web/pages/home-page.html";
    private static final String templateFilePath = "web/templates/users.tpl";
    private UserDao userDao;

    public HomeServlet(SessionFactory sessionFactory) {
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("templateFilePath = " + templateFilePath);
        String tplFilePath = Resources.getResourcePath(templateFilePath);
        logger.info("tplFilePath = " + tplFilePath);

        PrintWriter out = resp.getWriter();
        StringBuffer htmlPage = RelativeFileReader.getFileContent(filePath);
        logger.info("htmlPage = " + htmlPage);
        List<User> users = userDao.loadAll();
        logger.info("users = " + users);
        out.print(htmlPage.toString());
        WriterTemplate.writeTo(out, templateFilePath,"users", users);
    }
}
