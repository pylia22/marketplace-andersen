INSERT INTO categories (id, name, logo)
VALUES ('149ca028-fcc0-46d6-83a2-5dc0e3e92fd9', 'Smartphones', null),
       ('3c3a4982-296e-4446-84c2-aeff545cbc63', 'Laptops', null);

INSERT INTO products (id, name, logo, category_id)
VALUES ('6af7889e-d72b-4778-8fad-7429a5e253d4', 'iPhone 16 Pro', null, '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('d5b8083d-3748-400a-97a4-66b8b08d7406', 'iPhone 15 Pro', null, '149ca028-fcc0-46d6-83a2-5dc0e3e92fd9'),
       ('95ebfbaf-d117-46a6-83d4-2a3b1d3be296', 'Asus VivoBook', null, '3c3a4982-296e-4446-84c2-aeff545cbc63');