package alexander.ivanov.webserver.servlets;

import alexander.ivanov.webserver.models.hibernate.dao.UserDao;
import alexander.ivanov.webserver.models.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.webserver.models.hibernate.model.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class Auth extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = Optional.ofNullable(String.valueOf(req.getParameter("userName"))).orElse("");
        String userPassword = Optional.ofNullable(String.valueOf(req.getParameter("userPassword"))).orElse("");
        logger.info("!!!userName = " + userName);
        logger.info("!!!userPassword = " + userPassword);

        if (!userName.isEmpty() && !userPassword.isEmpty()) {
            try {
                User user = userDao.load(userName, userPassword);
                logger.info("loaded user = " + user);
                logger.info("req.isRequestedSessionIdFromCookie() = " + req.isRequestedSessionIdFromCookie());
                logger.info("req.isRequestedSessionIdFromURL() = " + req.isRequestedSessionIdFromURL());
                logger.info("req.isRequestedSessionIdValid() = " + req.isRequestedSessionIdValid());
                HttpSession session = req.getSession(true);
                session.setAttribute("user", userName);
                logger.info("session.getId() = {}, names = {}", session.getId(), session.getAttributeNames());
                resp.setStatus(HttpServletResponse.SC_OK);
                //logger.info("created session = " + session);

                resp.sendRedirect("/home");
                //jsonObject.addProperty("user", user.toString());
            } catch (Exception e) {
                logger.error(e.getMessage());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
