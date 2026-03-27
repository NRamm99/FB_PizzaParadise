package com.pizza.fb_pizzaparadise.web;

import com.pizza.fb_pizzaparadise.application.UserService;
import com.pizza.fb_pizzaparadise.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Pizzas/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "Pizzas/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            userService.createUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "Pizzas/register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Pizzas/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("user", user);

        int discount = (user.getPoints() / 100) * 10;
        model.addAttribute("discount", discount);

        model.addAttribute("favorites", userService.findFavoritePizzasByUserId(user.getId()));

        return "Pizzas/profile";
    }
}