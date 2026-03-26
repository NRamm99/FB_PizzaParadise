package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;

import java.util.ArrayList;
import java.util.List;

public class PizzaService {
    private static List<Pizza> pizzas = new ArrayList<>();

    public PizzaService() {
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    public void addTopping(Pizza pizza, PizzaTopping topping) {
        pizza.addTopping(topping);
    }

}
