CREATE SEQUENCE users_id_seq AS INT INCREMENT BY 1 MINVALUE 1;

CREATE TABLE IF NOT EXISTS "users" (
    id INT PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_login TIMESTAMP WITHOUT TIME ZONE,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS "roles" (
    id SERIAL PRIMARY KEY,
    role VARCHAR NOT NULL
);

INSERT INTO "roles" (role) VALUES ('USER');
INSERT INTO "roles" (role) VALUES ('ADMIN');

CREATE TABLE IF NOT EXISTS "users_roles" (
    user_id INT REFERENCES users (id),
    role_id INT REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);