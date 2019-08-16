package alexander.ivanov.controllers;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public void home(Model model) {
        logger.debug("HomeController.home");
        Collection<User> users = userService.findAll();
        model.addAttribute("users", users);
    }
}
