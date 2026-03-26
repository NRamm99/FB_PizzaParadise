package com.pizza.fb_pizzaparadise.service;

import com.pizza.fb_pizzaparadise.model.Pizza;
import com.pizza.fb_pizzaparadise.model.PizzaTopping;

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
