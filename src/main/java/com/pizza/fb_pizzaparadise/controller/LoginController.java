package com.pizza.fb_pizzaparadise.controller;

import com.pizza.fb_pizzaparadise.model.User;
import com.pizza.fb_pizzaparadise.service.UserService;
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

    @GetMapping("/")
    public String landingPage() {
        return "redirect:login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(Model model, @ModelAttribute User user) {
        model.addAttribute("user", user);
        // TEMP
        userService.createUser(user);
        return "dashboard";
    }

}
