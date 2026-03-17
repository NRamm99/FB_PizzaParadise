package com.pizza.fb_pizzaparadise.model;

import java.util.List;

public class OrderHistory {
    int id;
    List<Order> orders;

    public OrderHistory(int id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}
