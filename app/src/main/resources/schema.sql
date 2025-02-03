CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price DOUBLE NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0)
    );

INSERT INTO product (name, description, price, quantity)
VALUES('Laptop', 'Gaming laptop', 1500.00, 10), ('Smartphone', 'Android phone', 500.00, 30);
