package com.pizza.fb_pizzaparadise.domain.model;

import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;

import java.util.ArrayList;
import java.util.List;

public class Pizza {

    PizzaCrust crust;
    PizzaSauce sauce;
    PizzaCheese cheese;
    List<PizzaTopping> toppings;

    public Pizza(PizzaCrust crust, PizzaSauce sauce, PizzaCheese cheese) {
        this.crust = crust;
        this.sauce = sauce;
        this.cheese = cheese;
        this.toppings = new ArrayList<PizzaTopping>();
    }

    public void addTopping(PizzaTopping topping) {
        this.toppings.add(topping);
    }
}
