DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

CREATE TABLE games (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    favourite_game UUID DEFAULT NULL,
    username TEXT UNIQUE NOT NULL,
    coins INTEGER DEFAULT 100,
    password TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    created_at BIGINT NOT NULL,
    online BOOLEAN DEFAULT false,
    FOREIGN KEY (favourite_game) REFERENCES games (id)
);

CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE users_roles (
    user_id UUID,
    role_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
