package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.User;
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

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }
}
