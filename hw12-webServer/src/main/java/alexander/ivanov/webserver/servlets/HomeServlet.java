package alexander.ivanov.webserver.servlets;

import alexander.ivanov.webserver.models.hibernate.dao.UserDao;
import alexander.ivanov.webserver.models.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.webserver.models.hibernate.model.User;
import alexander.ivanov.webserver.util.RelativeDirectoryPath;
import alexander.ivanov.webserver.util.RelativeFileReader;
import alexander.ivanov.webserver.util.WriterTemplate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    private static final String filePath = "webapp" +  File.separator + "home-page.html";
    private static final String templateFilePath = RelativeDirectoryPath.get("templates/") + File.separator + "users.tpl";
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        config.getServletContext();
        super.init(config);
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        StringBuffer htmlPage = RelativeFileReader.getFileContent(filePath);
        List<User> users = userDao.loadAll();
        out.print(htmlPage.toString());
        WriterTemplate.writeTo(out, templateFilePath,"users", users);
    }
}
