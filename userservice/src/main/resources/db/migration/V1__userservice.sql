CREATE TABLE games (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    favourite_game UUID DEFAULT NULL,
    username TEXT UNIQUE NOT NULL,
    coins INTEGER DEFAULT 0,
    password TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    creation_date_epoch BIGINT NOT NULL,
    online BOOLEAN DEFAULT false,
    FOREIGN KEY (favourite_game) REFERENCES games (id)
);

CREATE TABLE roles (
    id UUID PRIMARY KEY,
    role TEXT NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id UUID,
    role_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE friendships (
    user_id_1 UUID,
    user_id_2 UUID,
    accepted BOOLEAN DEFAULT false,
    PRIMARY KEY (user_id_1, user_id_2)
)