package alexander.ivanov.fe.controllers.controllers;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.fe.services.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;
    private final HttpSession session;

    public HomeController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/home")
    public String home(Model model) {
        logger.debug("HomeController.home");
        logger.info("session = " + session);
        logger.info("session = " + session.getAttribute("name"));
        logger.info("session.getId() = " + session.getId());
        if (session.getAttribute("name") == null) {
            return "auth/login";
        } else {
            Iterator iter = session.getAttributeNames().asIterator();
            while (iter.hasNext()) {
                logger.info("iter.next() = " + iter.next());
            }
        }
        Collection<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "";
    }
}
