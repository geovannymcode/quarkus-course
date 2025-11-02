-- Usuario admin por defecto (password: admin123)
INSERT INTO users (id, username, email, password, full_name, active)
VALUES (1, 'admin', 'admin@bookmarker.com',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
        'Administrator', true);

INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN');
INSERT INTO user_roles (user_id, role) VALUES (1, 'USER');