package com.pizza.fb_pizzaparadise.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int points;
    private List<Pizza> favoritePizzas;

    public User() {
        this.favoritePizzas = new ArrayList<>();
        this.points = 0;
    }

    public User(int id, String name, String email, String password, int points) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
        this.favoritePizzas = new ArrayList<>();
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = 0;
        this.favoritePizzas = new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.points = 0;
        this.favoritePizzas = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Pizza> getFavoritePizzas() {
        return favoritePizzas;
    }

    public void setFavoritePizzas(List<Pizza> favoritePizzas) {
        this.favoritePizzas = favoritePizzas;
    }
}