DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username TEXT UNIQUE
);

CREATE TABLE friendships (
    user_id_1 UUID,
    user_id_2 UUID,
    accepted BOOLEAN NOT NULL,
    created_at BIGINT NOT NULL,

    PRIMARY KEY (user_id_1, user_id_2),
    FOREIGN KEY (user_id_1) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_2) REFERENCES users (id) ON DELETE CASCADE,
    -- Creo un constraint para no poder enviarte una solicitud de amista a ti mismo
    CONSTRAINT no_self_friendship CHECK (user_id_1 <> user_id_2)
)
