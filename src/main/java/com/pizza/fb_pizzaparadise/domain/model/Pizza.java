package com.pizza.fb_pizzaparadise.domain.model;

import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private int id;
    private String name;
    private PizzaCrust crust;
    private PizzaSauce sauce;
    private PizzaCheese cheese;
    private List<PizzaTopping> toppings;
    private int price;
    private boolean custom;

    public Pizza() {
        this.toppings = new ArrayList<>();
    }

    public Pizza(int id, String name, PizzaCrust crust, PizzaSauce sauce,
                 PizzaCheese cheese, List<PizzaTopping> toppings, int price, boolean custom) {
        this.id = id;
        this.name = name;
        this.crust = crust;
        this.sauce = sauce;
        this.cheese = cheese;
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        this.price = price;
        this.custom = custom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PizzaCrust getCrust() {
        return crust;
    }

    public void setCrust(PizzaCrust crust) {
        this.crust = crust;
    }

    public PizzaSauce getSauce() {
        return sauce;
    }

    public void setSauce(PizzaSauce sauce) {
        this.sauce = sauce;
    }

    public PizzaCheese getCheese() {
        return cheese;
    }

    public void setCheese(PizzaCheese cheese) {
        this.cheese = cheese;
    }

    public List<PizzaTopping> getToppings() {
        return toppings;
    }

    public void setToppings(List<PizzaTopping> toppings) {
        this.toppings = toppings;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}