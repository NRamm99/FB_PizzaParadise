package com.pizza.fb_pizzaparadise.domain.repository;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import java.util.List;

public interface PizzaRepository {

    List<Pizza> findAllMenuPizzas();

    Pizza findMenuPizzaById(int id);
}