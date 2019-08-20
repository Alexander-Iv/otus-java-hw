package alexander.ivanov.controllers;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserService userService;
    private final HttpSession session;

    public RegistrationController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping
    public void get() {
        logger.debug("RegistrationController.get");
    }

    @PostMapping
    public String post(Model model, String userName, String userPassword) {
        logger.debug("RegistrationController.post");
        logger.debug("session = {}", session);
        User newUser = new User(userName, userPassword);
        userService.add(newUser);
        model.addAttribute("users", userService.findAll());
        if(session.getAttribute("name") == null) {
            return "auth/login";
        }
        model.addAttribute("userName", session.getAttribute("name"));
        return "home";
    }
}
