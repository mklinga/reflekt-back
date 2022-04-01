CREATE SEQUENCE users_id_seq AS INT INCREMENT BY 1 MINVALUE 1;

CREATE TABLE IF NOT EXISTS "users" (
    id INT PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_login TIMESTAMP WITHOUT TIME ZONE,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL
);

INSERT INTO "users" (id, created_at, last_login, username, password) VALUES (
    nextval('users_id_seq'),
    '2021-01-25 10:04:00.000Z',
    NULL,
    'admin',
    '$2a$10$zxiLPtS0TZ7sDhCEInn6jOyI/UVGiz2Z9v.TOZRjYKLv4k6vzmwJC');

INSERT INTO "users" (id, created_at, last_login, username, password) VALUES (
    nextval('users_id_seq'),
    '2021-01-25 10:04:01.000Z',
    NULL,
    'laite',
    '$2a$10$nFJMUlG6nzgiXfbiTmy66.qc8o7tN0if27kL15PrnHQ9dRAQ40kXa');

CREATE TABLE IF NOT EXISTS "roles" (
    id INT PRIMARY KEY,
    role VARCHAR NOT NULL
);

INSERT INTO "roles" (id, role) VALUES (1, 'ADMIN');
INSERT INTO "roles" (id, role) VALUES (2, 'USER');

CREATE TABLE IF NOT EXISTS "users_roles" (
    user_id INT REFERENCES users (id),
    role_id INT REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO "users_roles" (user_id, role_id) VALUES (1, 1);
INSERT INTO "users_roles" (user_id, role_id) VALUES (1, 2);

INSERT INTO "users_roles" (user_id, role_id) VALUES (2, 1);
