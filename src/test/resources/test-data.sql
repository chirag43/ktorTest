DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       UUID NOT NULL PRIMARY KEY,
    username VARCHAR(45),
    password VARCHAR(50)
);

INSERT INTO users (id, username, password)
VALUES ('11111111-1111-1111-1111-111111111111', 'alice', 'password123'),
       ('22222222-2222-2222-2222-222222222222', 'bob', 'secret456'),
       ('33333333-3333-3333-3333-333333333333', 'charlie', 'qwerty789');
