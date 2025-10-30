DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username TEXT UNIQUE NOT NULL
);

DROP TABLE IF EXISTS friendships CASCADE;
CREATE TABLE friendships (
    user_id_1 UUID,
    user_id_2 UUID,
    accepted BOOLEAN DEFAULT false,
    created_at BIGINT DEFAULT EXTRACT(EPOCH FROM NOW())::BIGINT,

    PRIMARY KEY (user_id_1, user_id_2),
    FOREIGN KEY (user_id_1) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_2) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT no_self_friendship CHECK (user_id_1 <> user_id_2)
)
