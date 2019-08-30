package alexander.ivanov.fe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String getHome(Model model, HttpSession session) {
        logger.debug("HomeController.getHome");
        logger.info("session = " + session);
        logger.info("session.getAttribute(\"name\") = {}", session.getAttribute("name"));
        if (session == null || session.getAttribute("name") == null) {
            return "auth/login";
        }
        logger.info("session = " + session.getAttribute("name"));
        logger.info("session.getId() = " + session.getId());
        Iterator iter = session.getAttributeNames().asIterator();
        while (iter.hasNext()) {
            logger.info("iter.next() = " + iter.next());
        }
        //Collection<User> users = userService.findAll();
        //model.addAttribute("users", users);
        return "home";
    }

    @PostMapping("/home")
    public String postHome(Model model, HttpSession session, @RequestBody String body) {
        logger.info("HomeController.postHome");
        logger.info("body = {}", body);
        return getHome(model, session);
    }
}
