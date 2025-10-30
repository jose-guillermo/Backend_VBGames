DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE games (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE pieces (
    game_id UUID NOT NULL,
    name TEXT NOT NULL,
    color TEXT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    PRIMARY KEY (game_id, name, color)
);