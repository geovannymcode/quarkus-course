-- Tabla de usuarios
CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 101 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT NOT NULL DEFAULT nextval('user_id_seq'),
                                     username VARCHAR(50) NOT NULL,
                                     email VARCHAR(255) NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     full_name VARCHAR(100),
                                     active BOOLEAN NOT NULL DEFAULT TRUE,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     PRIMARY KEY (id),
                                     CONSTRAINT uk_users_username UNIQUE (username),
                                     CONSTRAINT uk_users_email UNIQUE (email)
);

-- Tabla de roles de usuario
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role VARCHAR(50) NOT NULL,
                                          CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                          CONSTRAINT uk_user_role UNIQUE (user_id, role)
);