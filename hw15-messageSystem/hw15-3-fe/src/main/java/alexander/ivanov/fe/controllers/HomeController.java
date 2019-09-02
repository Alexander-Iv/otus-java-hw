package alexander.ivanov.fe.controllers;

import alexander.ivanov.fe.model.Users;
import alexander.ivanov.fe.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

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
        logger.info("model.addAttribute(\"users\") = {}", model.addAttribute("users"));
        Iterator iter = session.getAttributeNames().asIterator();
        while (iter.hasNext()) {
            logger.info("iter.next() = " + iter.next());
        }
        //Collection<User> users = userService.findAll();
        //model.addAttribute("users", users);
        logger.info("redirect to home");
        return "home";
    }

    @PostMapping("/home")
    public RedirectView postHome(Model model, HttpSession session, @RequestBody String body) {
        logger.info("HomeController.postHome");
        logger.info("body = {}", body);
        logger.info("model = {}", model);
        logger.info("session = {}", session);

        Users users = JsonHelper.readUsersFromJson(body);
        logger.info("users = {}", users);
        if (users != null) {
            logger.info("users:");
            users.getUsers().forEach(user -> {
                logger.info("user = {}", user);
            });
        }

        model.addAttribute("users", users.getUsers());

        //return getHome(model, session);
        return new RedirectView("home", true);
    }


}
