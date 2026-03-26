package com.pizza.fb_pizzaparadise.web;

import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.application.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "Pizzas/login";
    }

    @PostMapping("/login")
    public String submitLogin(Model model, @ModelAttribute User user) {
        model.addAttribute("user", user);
        // TEMP
        userService.createUser(user);
        return "Pizzas/dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "Pizzas/register";
    }
}
