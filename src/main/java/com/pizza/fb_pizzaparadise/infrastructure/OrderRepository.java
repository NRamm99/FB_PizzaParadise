package com.pizza.fb_pizzaparadise.infrastructure;

import com.pizza.fb_pizzaparadise.domain.model.Order;
import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveOrder(Order order) {
        String sql = """
                INSERT INTO orders (user_id, total_price, discount_used, points_earned, points_spent)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getTotalPrice());
            ps.setInt(3, order.getDiscountUsed());
            ps.setInt(4, order.getPointsEarned());
            ps.setInt(5, order.getPointsSpent());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void saveOrderItems(int orderId, List<Pizza> pizzas) {
        String sql = """
                INSERT INTO order_items (order_id, pizza_name, crust, sauce, cheese, toppings, price, is_custom)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        for (Pizza pizza : pizzas) {
            String toppings = pizza.getToppings() == null || pizza.getToppings().isEmpty()
                    ? ""
                    : String.join(", ", pizza.getToppings().stream().map(Enum::name).toList());

            jdbcTemplate.update(
                    sql,
                    orderId,
                    pizza.getName(),
                    pizza.getCrust().name(),
                    pizza.getSauce().name(),
                    pizza.getCheese().name(),
                    toppings,
                    pizza.getPrice(),
                    pizza.isCustom()
            );
        }
    }

    public Order findOrderById(int orderId) {
        String sql = """
                SELECT id, user_id, total_price, discount_used, points_earned, points_spent, created_at
                FROM orders
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getInt("total_price"));
                order.setDiscountUsed(rs.getInt("discount_used"));
                order.setPointsEarned(rs.getInt("points_earned"));
                order.setPointsSpent(rs.getInt("points_spent"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    order.setCreatedAt(ts.toLocalDateTime());
                }
                order.setPizzas(findItemsByOrderId(order.getId()));
                return order;
            }
            return null;
        }, orderId);
    }

    public List<Order> findOrdersByUserId(int userId) {
        String sql = """
                SELECT id, user_id, total_price, discount_used, points_earned, points_spent, created_at
                FROM orders
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setUserId(rs.getInt("user_id"));
            order.setTotalPrice(rs.getInt("total_price"));
            order.setDiscountUsed(rs.getInt("discount_used"));
            order.setPointsEarned(rs.getInt("points_earned"));
            order.setPointsSpent(rs.getInt("points_spent"));

            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) {
                order.setCreatedAt(ts.toLocalDateTime());
            }

            order.setPizzas(findItemsByOrderId(order.getId()));
            return order;
        }, userId);
    }

    private List<Pizza> findItemsByOrderId(int orderId) {
        String sql = """
                SELECT pizza_name, crust, sauce, cheese, toppings, price, is_custom
                FROM order_items
                WHERE order_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Pizza pizza = new Pizza();
            pizza.setName(rs.getString("pizza_name"));
            pizza.setCrust(com.pizza.fb_pizzaparadise.domain.types.PizzaCrust.valueOf(rs.getString("crust")));
            pizza.setSauce(com.pizza.fb_pizzaparadise.domain.types.PizzaSauce.valueOf(rs.getString("sauce")));
            pizza.setCheese(com.pizza.fb_pizzaparadise.domain.types.PizzaCheese.valueOf(rs.getString("cheese")));

            String toppingsString = rs.getString("toppings");
            if (toppingsString == null || toppingsString.isBlank()) {
                pizza.setToppings(new ArrayList<>());
            } else {
                pizza.setToppings(Arrays.stream(toppingsString.split(", "))
                        .map(com.pizza.fb_pizzaparadise.domain.types.PizzaTopping::valueOf)
                        .toList());
            }

            pizza.setPrice(rs.getInt("price"));
            pizza.setCustom(rs.getBoolean("is_custom"));
            return pizza;
        }, orderId);
    }
}