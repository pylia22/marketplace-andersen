CREATE TABLE IF NOT EXISTS categories (
    id   BIGINT  NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    logo VARCHAR
);

CREATE TABLE IF NOT EXISTS products (
    id   BIGINT  NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    logo VARCHAR,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

INSERT INTO categories VALUES (1, 'Smartphones', null), (2, 'Laptops', null);

INSERT INTO products VALUES (1, 'iPhone 16 Pro', null, 1), (2, 'iPhone 15 Pro', null, 1), (3, 'Asus VivoBook', null, 2);