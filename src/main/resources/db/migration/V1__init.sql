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
VALUES ('149ca028-fcc0-46d6-83a2-5dc0e3e92fd9', 'Smartphones', '9447fadb-4f67-4d2d-8c84-3929ab6b3a6a_Screenshot 2024-11-07 133556.png'),
       ('3c3a4982-296e-4446-84c2-aeff545cbc63', 'Laptops', '294932fa-836d-4b5d-9730-39dcf9102ee8_Screenshot 2024-11-04 131742.png');

INSERT INTO products (id, name, logo, category_id)
VALUES ('6af7889e-d72b-4778-8fad-7429a5e253d4', 'iPhone 16 Pro', 'a04e324d-e589-4edf-b91f-824e26d5c8ed_board-361516_640.jpg', '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('d5b8083d-3748-400a-97a4-66b8b08d7406', 'iPhone 15 Pro', 'b2e40b99-85b5-4878-8748-2a33c7f64eee_Screenshot 2024-10-16 202716.png', '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('95ebfbaf-d117-46a6-83d4-2a3b1d3be296', 'Asus VivoBook', 'c5359ca4-31f4-4714-80f8-e222cec9e209_Screenshot 2024-10-15 185525.png', '3c3a4982-296e-4446-84c2-aeff545cbc63');

INSERT INTO users
VALUES ('21178d15-67b2-4880-98b2-50f1653321d0', 'admin', '$2a$10$Ll/I47FxR00F61HXVRfU4eECPTH7k6IbTA/3h4pBevkAj3vLRwrMG', 'ROLE_EDITOR, ROLE_USER'),
       ('7a059c4f-2732-4614-a9c8-466e6d2a65e5', 'user', '$2a$10$nx3Gu3e22GEZEpoYFz7AfeubGjNXiett7MFsfS2o979oy7za54u9S', 'ROLE_USER');