package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Order;
import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCheese;
import com.pizza.fb_pizzaparadise.domain.types.PizzaCrust;
import com.pizza.fb_pizzaparadise.domain.types.PizzaSauce;
import com.pizza.fb_pizzaparadise.domain.types.PizzaTopping;
import com.pizza.fb_pizzaparadise.infrastructure.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrderAppliesDiscountPersistsItemsAndUpdatesPoints() {
        User user = new User(7, "Nick", "mail@test.com", "pw", 250);
        List<Pizza> cart = List.of(
                new Pizza(1, "A", PizzaCrust.THIN, PizzaSauce.TOMATO, PizzaCheese.MOZZARELLA, List.of(), 70, false),
                new Pizza(2, "B", PizzaCrust.THICK, PizzaSauce.BBQ, PizzaCheese.CHEDDAR, List.of(PizzaTopping.BACON), 90, true)
        );
        Order persistedOrder = new Order();
        persistedOrder.setId(99);

        when(userService.calculateDiscount(250)).thenReturn(20);
        when(userService.calculatePointsSpentFromDiscount(20)).thenReturn(200);
        when(userService.calculatePointsEarned(140)).thenReturn(14);
        when(orderRepository.saveOrder(org.mockito.ArgumentMatchers.any(Order.class))).thenReturn(99);
        when(orderRepository.findOrderById(99)).thenReturn(persistedOrder);

        Order result = orderService.placeOrder(user, cart, true);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals(7, savedOrder.getUserId());
        assertSame(cart, savedOrder.getPizzas());
        assertEquals(20, savedOrder.getDiscountUsed());
        assertEquals(140, savedOrder.getTotalPrice());
        assertEquals(200, savedOrder.getPointsSpent());
        assertEquals(14, savedOrder.getPointsEarned());
        verify(orderRepository).saveOrderItems(99, cart);
        verify(userService).updatePoints(7, 64);
        assertSame(persistedOrder, result);
    }

    @Test
    void placeOrderDoesNotApplyDiscountWhenFlagIsFalse() {
        User user = new User(3, "Nick", "mail@test.com", "pw", 250);
        List<Pizza> cart = List.of(
                new Pizza(1, "A", PizzaCrust.THIN, PizzaSauce.TOMATO, PizzaCheese.MOZZARELLA, List.of(), 70, false)
        );
        Order persistedOrder = new Order();

        when(userService.calculateDiscount(250)).thenReturn(20);
        when(userService.calculatePointsSpentFromDiscount(0)).thenReturn(0);
        when(userService.calculatePointsEarned(70)).thenReturn(7);
        when(orderRepository.saveOrder(org.mockito.ArgumentMatchers.any(Order.class))).thenReturn(5);
        when(orderRepository.findOrderById(5)).thenReturn(persistedOrder);

        orderService.placeOrder(user, cart, false);

        verify(userService).updatePoints(3, 257);
    }

    @Test
    void delegatesFindMethodsToRepository() {
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderRepository.findOrderById(11)).thenReturn(order);
        when(orderRepository.findOrdersByUserId(8)).thenReturn(orders);

        assertSame(order, orderService.findOrderById(11));
        assertSame(orders, orderService.findOrdersByUserId(8));
    }
}
