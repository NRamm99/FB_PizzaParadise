package com.pizza.fb_pizzaparadise.web;

import com.pizza.fb_pizzaparadise.application.OrderService;
import com.pizza.fb_pizzaparadise.application.PizzaService;
import com.pizza.fb_pizzaparadise.application.UserService;
import com.pizza.fb_pizzaparadise.domain.model.Order;
import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
@SessionAttributes({"cart", "discountApplied"})
public class OrderController {

    private final PizzaService pizzaService;
    private final UserService userService;
    private final OrderService orderService;

    public OrderController(PizzaService pizzaService, UserService userService, OrderService orderService) {
        this.pizzaService = pizzaService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @ModelAttribute("cart")
    public List<Pizza> cart() {
        return new ArrayList<>();
    }

    @ModelAttribute("discountApplied")
    public Boolean discountApplied() {
        return false;
    }

    @GetMapping
    public String orderPage(Model model,
                            Authentication authentication,
                            @ModelAttribute("cart") List<Pizza> cart,
                            @ModelAttribute("discountApplied") Boolean discountApplied) {

        User user = userService.findByEmail(authentication.getName());
        List<Pizza> favorites = userService.findFavoritePizzasByUserId(user.getId());

        int cartTotal = pizzaService.calculateCartTotal(cart);
        int availableDiscount = userService.calculateDiscount(user.getPoints());
        int appliedDiscount = Boolean.TRUE.equals(discountApplied) ? availableDiscount : 0;
        int finalTotal = Math.max(cartTotal - appliedDiscount, 0);

        model.addAttribute("menu", pizzaService.getDefaultMenu());
        model.addAttribute("favorites", favorites);
        model.addAttribute("crusts", PizzaCrust.values());
        model.addAttribute("sauces", PizzaSauce.values());
        model.addAttribute("cheeses", PizzaCheese.values());
        model.addAttribute("toppings", PizzaTopping.values());

        model.addAttribute("user", user);
        model.addAttribute("availableDiscount", availableDiscount);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("appliedDiscount", appliedDiscount);
        model.addAttribute("finalTotal", finalTotal);

        return "Pizzas/order";
    }

    @PostMapping("/add/{id}")
    public String addDefaultPizza(@PathVariable int id,
                                  @ModelAttribute("cart") List<Pizza> cart) {
        Pizza pizza = pizzaService.findDefaultPizzaById(id);

        if (pizza != null) {
            cart.add(pizza);
        }

        return "redirect:/order";
    }

    @PostMapping("/custom")
    public String addCustomPizza(@RequestParam String name,
                                 @RequestParam PizzaCrust crust,
                                 @RequestParam PizzaSauce sauce,
                                 @RequestParam PizzaCheese cheese,
                                 @RequestParam(required = false) List<PizzaTopping> toppings,
                                 @RequestParam(defaultValue = "false") boolean favorite,
                                 Authentication authentication,
                                 @ModelAttribute("cart") List<Pizza> cart) {

        Pizza customPizza = pizzaService.createCustomPizza(name, crust, sauce, cheese, toppings);
        cart.add(customPizza);

        if (favorite) {
            User user = userService.findByEmail(authentication.getName());
            userService.saveFavoritePizza(user.getId(), customPizza);
        }

        return "redirect:/order";
    }

    @PostMapping("/favorite/{index}")
    public String addFavoriteToCart(@PathVariable int index,
                                    Authentication authentication,
                                    @ModelAttribute("cart") List<Pizza> cart) {

        User user = userService.findByEmail(authentication.getName());
        List<Pizza> favorites = userService.findFavoritePizzasByUserId(user.getId());

        if (index >= 0 && index < favorites.size()) {
            cart.add(favorites.get(index));
        }

        return "redirect:/order";
    }

    @PostMapping("/clear")
    public String clearCart(@ModelAttribute("cart") List<Pizza> cart, Model model) {
        cart.clear();
        model.addAttribute("discountApplied", false);
        return "redirect:/order";
    }

    @PostMapping("/apply-discount")
    public String applyDiscount(Model model) {
        model.addAttribute("discountApplied", true);
        return "redirect:/order";
    }

    @PostMapping("/remove-discount")
    public String removeDiscount(Model model) {
        model.addAttribute("discountApplied", false);
        return "redirect:/order";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication,
                           @ModelAttribute("cart") List<Pizza> cart,
                           @ModelAttribute("discountApplied") Boolean discountApplied,
                           SessionStatus sessionStatus) {

        if (cart.isEmpty()) {
            return "redirect:/order";
        }

        User user = userService.findByEmail(authentication.getName());
        Order savedOrder = orderService.placeOrder(user, cart, Boolean.TRUE.equals(discountApplied));

        sessionStatus.setComplete();

        return "redirect:/order/receipt/" + savedOrder.getId();
    }

    @GetMapping("/receipt/{orderId}")
    public String receipt(@PathVariable int orderId, Model model) {
        Order order = orderService.findOrderById(orderId);
        model.addAttribute("order", order);
        return "Pizzas/receipt";
    }

    @GetMapping("/history")
    public String orderHistory(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        List<Order> orders = orderService.findOrdersByUserId(user.getId());

        model.addAttribute("orders", orders);
        return "Pizzas/order-history";
    }
}