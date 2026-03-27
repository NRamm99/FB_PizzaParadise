package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.infrastructure.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("A user with that email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPoints(0);

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int calculateDiscount(int points) {
        return (points / 100) * 10;
    }

    public int calculatePointsSpentFromDiscount(int discountAmount) {
        return discountAmount * 10;
    }

    public int calculatePointsEarned(int totalPaid) {
        return totalPaid / 10;
    }

    public void updatePoints(int userId, int newPoints) {
        userRepository.updatePoints(userId, newPoints);
    }

    public void saveFavoritePizza(int userId, Pizza pizza) {
        userRepository.saveFavoritePizza(userId, pizza);
    }

    public java.util.List<Pizza> findFavoritePizzasByUserId(int userId) {
        return userRepository.findFavoritePizzasByUserId(userId);
    }
}