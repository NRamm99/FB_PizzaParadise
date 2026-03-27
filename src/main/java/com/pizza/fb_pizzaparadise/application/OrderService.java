package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Order;
import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.infrastructure.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public Order placeOrder(User user, List<Pizza> cart, boolean discountApplied) {
        int subtotal = cart.stream().mapToInt(Pizza::getPrice).sum();
        int availableDiscount = userService.calculateDiscount(user.getPoints());
        int usedDiscount = discountApplied ? availableDiscount : 0;
        int finalTotal = Math.max(subtotal - usedDiscount, 0);

        int pointsSpent = userService.calculatePointsSpentFromDiscount(usedDiscount);
        int pointsEarned = userService.calculatePointsEarned(finalTotal);
        int newPoints = user.getPoints() - pointsSpent + pointsEarned;

        Order order = new Order();
        order.setUserId(user.getId());
        order.setPizzas(cart);
        order.setDiscountUsed(usedDiscount);
        order.setTotalPrice(finalTotal);
        order.setPointsSpent(pointsSpent);
        order.setPointsEarned(pointsEarned);

        int orderId = orderRepository.saveOrder(order);
        orderRepository.saveOrderItems(orderId, cart);
        userService.updatePoints(user.getId(), newPoints);

        return orderRepository.findOrderById(orderId);
    }

    public Order findOrderById(int orderId) {
        return orderRepository.findOrderById(orderId);
    }

    public List<Order> findOrdersByUserId(int userId) {
        return orderRepository.findOrdersByUserId(userId);
    }
}