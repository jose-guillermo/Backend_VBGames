DROP TABLE IF EXISTS games CASCADE;
CREATE TABLE games (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id UUID PRIMARY KEY,
    favourite_game UUID DEFAULT NULL,
    username TEXT UNIQUE NOT NULL,
    coins INTEGER DEFAULT 100,
    password TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    creation_date_epoch BIGINT NOT NULL,
    online BOOLEAN DEFAULT false,
    FOREIGN KEY (favourite_game) REFERENCES games (id)
);

DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS users_roles CASCADE;
CREATE TABLE users_roles (
    user_id UUID,
    role_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

-- Permite la creación de uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";