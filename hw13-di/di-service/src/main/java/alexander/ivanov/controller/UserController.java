package alexander.ivanov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        logger.debug("UserController.list");
        logger.debug("model = {}", model);
        model.addAllAttributes(Arrays.asList("qwerty", "asdfgh", "zxcvbn"));
        return "users/list";
    }
}
