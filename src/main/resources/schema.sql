
CREATE TABLE IF NOT EXISTS users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       points INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS pizzas (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        crust VARCHAR(50) NOT NULL,
                        sauce VARCHAR(50) NOT NULL,
                        cheese VARCHAR(50) NOT NULL,
                        toppings VARCHAR(255),
                        price INT NOT NULL,
                        is_custom BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        total_price INT NOT NULL,
                        discount_used INT NOT NULL DEFAULT 0,
                        points_earned INT NOT NULL DEFAULT 0,
                        points_spent INT NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT NOT NULL,
                             pizza_name VARCHAR(100) NOT NULL,
                             crust VARCHAR(50) NOT NULL,
                             sauce VARCHAR(50) NOT NULL,
                             cheese VARCHAR(50) NOT NULL,
                             toppings VARCHAR(255),
                             price INT NOT NULL,
                             is_custom BOOLEAN NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE IF NOT EXISTS favorite_pizzas (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 user_id INT NOT NULL,
                                 pizza_name VARCHAR(100) NOT NULL,
                                 crust VARCHAR(50) NOT NULL,
                                 sauce VARCHAR(50) NOT NULL,
                                 cheese VARCHAR(50) NOT NULL,
                                 toppings VARCHAR(255),
                                 price INT NOT NULL,
                                 FOREIGN KEY (user_id) REFERENCES users(id)
);