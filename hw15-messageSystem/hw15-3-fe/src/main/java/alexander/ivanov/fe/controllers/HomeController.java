package alexander.ivanov.fe.controllers;

import alexander.ivanov.fe.model.Users;
import alexander.ivanov.fe.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Controller
@SessionScope
@SessionAttributes(types = {Users.class})
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String getHome(Model model, HttpServletRequest request, HttpSession session, @ModelAttribute("users") Users users) {
        logger.debug("HomeController.getHome");
        logger.info("session = " + session);
        logger.info("session.getAttribute(\"name\") = {}", session.getAttribute("name"));
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        logger.info("attributes.getRequest().getSession(false).getAttribute(\"name\") = {}", attributes.getRequest().getSession(false).getAttribute("name"));
        logger.info("request.getSession(false).getAttribute(\"name\") = {}", request.getSession(false).getAttribute("name"));
        if (session == null || session.getAttribute("name") == null) {
            return "auth/login";
        }
        logger.info("session = " + session.getAttribute("name"));
        logger.info("session.getId() = " + session.getId());
        Iterator iter = session.getAttributeNames().asIterator();
        logger.info("gh MODEL:");
        model.asMap().forEach((s, o) -> {
            logger.info("s = {}, o = {}", s, o);
        });
        while (iter.hasNext()) {
            logger.info("iter.next() = " + iter.next());
        }
        logger.info("GET users:");
        if (users.getUsers() != null) {
            users.getUsers().forEach(user -> {
                logger.info("user = {}", user);
            });
        }
        logger.info("redirect to home");
        return "home";
    }

    @PostMapping("/home")
    public RedirectView postHome(@RequestBody String body, RedirectAttributes attributes) {
        logger.info("HomeController.postHome");
        logger.info("body = {}", body);
        logger.info("attributes = {}", attributes);

        Users users = JsonHelper.readUsersFromJson(body);
        logger.info("POST users = {}", users);
        if (users != null) {
            logger.info("users:");
            users.getUsers().forEach(user -> {
                logger.info("user = {}", user);
            });
        }
        attributes.addFlashAttribute("users", users);
        RedirectView redirect = new RedirectView("home");
        redirect.setAttributesMap(attributes.getFlashAttributes());
        logger.info("redirect = {}", redirect);
        return redirect;
    }

    @ModelAttribute("users")
    public Users getUsers() {
        return new Users();
    }
}
