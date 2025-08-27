DROP TABLE IF EXISTS stocks;

CREATE TABLE stocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cod VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock_value DOUBLE,
    percentage FLOAT,
    qtd INT
);