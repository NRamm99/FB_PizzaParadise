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

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
