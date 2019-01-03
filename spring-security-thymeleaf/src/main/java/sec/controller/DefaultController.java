package sec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({ "/index", "/" })
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/secinfo")
    public String secinfo() {
        return "secinfo";
    }
    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
}
