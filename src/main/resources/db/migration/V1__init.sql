CREATE TABLE IF NOT EXISTS categories
(
    id   UUID    NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    logo VARCHAR
);

CREATE TABLE IF NOT EXISTS products
(
    id          UUID    NOT NULL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    logo        VARCHAR,
    category_id UUID,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id       UUID  NOT NULL PRIMARY KEY,
    name     VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    roles    VARCHAR
);

INSERT INTO categories (id, name, logo)
VALUES ('149ca028-fcc0-46d6-83a2-5dc0e3e92fd9', 'Smartphones', null),
       ('3c3a4982-296e-4446-84c2-aeff545cbc63', 'Laptops', null);

INSERT INTO products (id, name, logo, category_id)
VALUES ('6af7889e-d72b-4778-8fad-7429a5e253d4', 'iPhone 16 Pro', null, '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('d5b8083d-3748-400a-97a4-66b8b08d7406', 'iPhone 15 Pro', null, '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('95ebfbaf-d117-46a6-83d4-2a3b1d3be296', 'Asus VivoBook', null, '3c3a4982-296e-4446-84c2-aeff545cbc63');

INSERT INTO users
VALUES ('21178d15-67b2-4880-98b2-50f1653321d0', 'admin', '$2a$10$Ll/I47FxR00F61HXVRfU4eECPTH7k6IbTA/3h4pBevkAj3vLRwrMG', 'ROLE_EDITOR, ROLE_USER'),
       ('7a059c4f-2732-4614-a9c8-466e6d2a65e5', 'user', '$2a$10$nx3Gu3e22GEZEpoYFz7AfeubGjNXiett7MFsfS2o979oy7za54u9S', 'ROLE_USER');