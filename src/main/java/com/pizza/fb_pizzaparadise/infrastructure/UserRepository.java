package com.pizza.fb_pizzaparadise.infrastructure;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password, points) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPoints()
        );
    }

    public User findByEmail(String email) {
        String sql = "SELECT id, name, email, password, points FROM users WHERE email = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPoints(rs.getInt("points"));
                return user;
            }
            return null;
        }, email);
    }

    public void updatePoints(int userId, int newPoints) {
        String sql = "UPDATE users SET points = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPoints, userId);
    }

    public void saveFavoritePizza(int userId, Pizza pizza) {
        String sql = """
            INSERT INTO favorite_pizzas (user_id, pizza_name, crust, sauce, cheese, toppings, price)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        String toppings = pizza.getToppings() == null || pizza.getToppings().isEmpty()
                ? ""
                : String.join(", ", pizza.getToppings().stream().map(Enum::name).toList());

        jdbcTemplate.update(
                sql,
                userId,
                pizza.getName(),
                pizza.getCrust().name(),
                pizza.getSauce().name(),
                pizza.getCheese().name(),
                toppings,
                pizza.getPrice()
        );
    }

    public List<Pizza> findFavoritePizzasByUserId(int userId) {
        String sql = """
            SELECT pizza_name, crust, sauce, cheese, toppings, price
            FROM favorite_pizzas
            WHERE user_id = ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Pizza pizza = new Pizza();
            pizza.setName(rs.getString("pizza_name"));
            pizza.setCrust(com.pizza.fb_pizzaparadise.domain.types.PizzaCrust.valueOf(rs.getString("crust")));
            pizza.setSauce(com.pizza.fb_pizzaparadise.domain.types.PizzaSauce.valueOf(rs.getString("sauce")));
            pizza.setCheese(com.pizza.fb_pizzaparadise.domain.types.PizzaCheese.valueOf(rs.getString("cheese")));

            String toppingsString = rs.getString("toppings");
            if (toppingsString == null || toppingsString.isBlank()) {
                pizza.setToppings(new java.util.ArrayList<>());
            } else {
                pizza.setToppings(java.util.Arrays.stream(toppingsString.split(", "))
                        .map(com.pizza.fb_pizzaparadise.domain.types.PizzaTopping::valueOf)
                        .toList());
            }

            pizza.setPrice(rs.getInt("price"));
            pizza.setCustom(true);
            return pizza;
        }, userId);
    }
}