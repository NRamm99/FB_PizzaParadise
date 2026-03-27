package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PizzaServiceTest {

    private final PizzaService pizzaService = new PizzaService();

    @Test
    void getDefaultMenuReturnsFiveStandardPizzas() {
        List<Pizza> menu = pizzaService.getDefaultMenu();

        assertEquals(5, menu.size());
        assertEquals("Margherita", menu.get(0).getName());
        assertEquals("Meat Lover", menu.get(4).getName());
        assertTrue(menu.stream().noneMatch(Pizza::isCustom));
    }

    @Test
    void findDefaultPizzaByIdReturnsMatchingPizza() {
        Pizza pizza = pizzaService.findDefaultPizzaById(2);

        assertNotNull(pizza);
        assertEquals("Pepperoni", pizza.getName());
        assertEquals(85, pizza.getPrice());
    }

    @Test
    void findDefaultPizzaByIdReturnsNullWhenMissing() {
        assertNull(pizzaService.findDefaultPizzaById(99));
    }

    @Test
    void createCustomPizzaUsesFallbackNameAndCalculatesPriceFromToppings() {
        Pizza pizza = pizzaService.createCustomPizza(
                " ",
                PizzaCrust.THICK,
                PizzaSauce.BBQ,
                PizzaCheese.CHEDDAR,
                List.of(PizzaTopping.BACON, PizzaTopping.ONION)
        );

        assertEquals("Custom Pizza", pizza.getName());
        assertEquals(80, pizza.getPrice());
        assertTrue(pizza.isCustom());
        assertEquals(2, pizza.getToppings().size());
    }

    @Test
    void createCustomPizzaHandlesNullToppings() {
        Pizza pizza = pizzaService.createCustomPizza(
                "Builder",
                PizzaCrust.THIN,
                PizzaSauce.TOMATO,
                PizzaCheese.MOZZARELLA,
                null
        );

        assertEquals("Builder", pizza.getName());
        assertEquals(60, pizza.getPrice());
        assertNotNull(pizza.getToppings());
        assertTrue(pizza.getToppings().isEmpty());
    }

    @Test
    void calculateCartTotalSumsAllPizzaPrices() {
        List<Pizza> cart = List.of(
                new Pizza(1, "A", PizzaCrust.THIN, PizzaSauce.TOMATO, PizzaCheese.MOZZARELLA, List.of(), 70, false),
                new Pizza(2, "B", PizzaCrust.THICK, PizzaSauce.BBQ, PizzaCheese.CHEDDAR, List.of(PizzaTopping.BACON), 95, true)
        );

        assertEquals(165, pizzaService.calculateCartTotal(cart));
    }
}
