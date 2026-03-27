package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaService {

    public List<Pizza> getDefaultMenu() {
        List<Pizza> pizzas = new ArrayList<>();

        pizzas.add(new Pizza(
                1,
                "Margherita",
                PizzaCrust.THIN,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                List.of(),
                70,
                false
        ));

        pizzas.add(new Pizza(
                2,
                "Pepperoni",
                PizzaCrust.THIN,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                List.of(PizzaTopping.PEPPERONI),
                85,
                false
        ));

        pizzas.add(new Pizza(
                3,
                "Hawaiian",
                PizzaCrust.THIN,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                List.of(PizzaTopping.HAM, PizzaTopping.PINEAPPLE),
                85,
                false
        ));

        pizzas.add(new Pizza(
                4,
                "Veggie",
                PizzaCrust.THIN,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                List.of(PizzaTopping.MUSHROOM, PizzaTopping.ONION, PizzaTopping.PEPPER),
                90,
                false
        ));

        pizzas.add(new Pizza(
                5,
                "Meat Lover",
                PizzaCrust.THICK,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                List.of(PizzaTopping.PEPPERONI, PizzaTopping.HAM, PizzaTopping.BACON),
                100,
                false
        ));

        return pizzas;
    }

    public Pizza findDefaultPizzaById(int id) {
        return getDefaultMenu().stream()
                .filter(pizza -> pizza.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Pizza createCustomPizza(String name,
                                   PizzaCrust crust,
                                   PizzaSauce sauce,
                                   PizzaCheese cheese,
                                   List<PizzaTopping> toppings) {

        List<PizzaTopping> safeToppings = toppings != null ? toppings : new ArrayList<>();

        int basePrice = 60;
        int toppingPrice = safeToppings.size() * 10;
        int totalPrice = basePrice + toppingPrice;

        Pizza pizza = new Pizza();
        pizza.setName((name == null || name.isBlank()) ? "Custom Pizza" : name);
        pizza.setCrust(crust);
        pizza.setSauce(sauce);
        pizza.setCheese(cheese);
        pizza.setToppings(safeToppings);
        pizza.setPrice(totalPrice);
        pizza.setCustom(true);

        return pizza;
    }

    public int calculateCartTotal(List<Pizza> cart) {
        return cart.stream()
                .mapToInt(Pizza::getPrice)
                .sum();
    }
}