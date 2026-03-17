package com.pizza.fb_pizzaparadise.service;

import com.pizza.fb_pizzaparadise.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final List<User> users = new ArrayList<>();

    public UserService() {
    }

    public void createUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }
}
