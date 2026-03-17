package com.pizza.fb_pizzaparadise.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    String name;
    String email;
    String password;
    int points;
    List<Pizza> favoritePizzas;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favoritePizzas = new ArrayList<Pizza>();
        this.points = 0;
    }

}
