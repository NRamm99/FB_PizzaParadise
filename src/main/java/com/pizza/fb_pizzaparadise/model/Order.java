package com.pizza.fb_pizzaparadise.model;

import java.util.List;

public class Order {
    int id;
    String name;
    List<Pizza> pizzas;

    public Order(int id, String name, List<Pizza> pizzas) {
        this.id = id;
        this.name = name;
        this.pizzas = pizzas;
    }
}
