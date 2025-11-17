DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS messages CASCADE;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username TEXT NOT NULL UNIQUE
);

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    sender_id UUID NOT NULL,
    recipient_id UUID NOT NULL,
    title TEXT,
    send_date BIGINT NOT NULL,
    is_read bool NOT NULL,
    body TEXT,
    type TEXT NOT NULL,

    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE
);
