package alexander.ivanov.webserver.servlets;

import alexander.ivanov.webserver.models.hibernate.dao.UserDao;
import alexander.ivanov.webserver.models.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.webserver.models.hibernate.model.User;
import alexander.ivanov.webserver.util.RelativeDirectoryPath;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class Home extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    private static final String filePath = RelativeDirectoryPath.get("webapp/") + File.separator + "home-page.html";
    private UserDao userDao;
    private String macro = "${USERS}";

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
        StringBuffer htmlPage = new StringBuffer();
        StringBuffer usersTab = new StringBuffer();

        //костылище? :)
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)))) {
            int buffer;
            while ((buffer = bis.read()) > 0) {
                htmlPage.append(Character.toString(buffer));
            }
        }

        List<User> users = userDao.loadAll();
        if (!users.isEmpty()) {
            usersTab.append("<th>").append("User Id").append("</th>")
                    .append("<th>").append("User Name").append("</th>");
            users.forEach(user -> {
                usersTab.append("<tr>")
                        .append("<td>").append(user.getId()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("</tr>");
            });
            htmlPage.replace(htmlPage.indexOf(macro), htmlPage.indexOf(macro) + macro.length(), usersTab.toString());
        }

        out.print(htmlPage.toString());

        //getRequestDispatcher возвращает null, не понимаю почему.. возможно дело в Jetty
        /*RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/home-page");
        dispatcher.include(req, resp);
        dispatcher.forward(req, resp);*/
    }
}
