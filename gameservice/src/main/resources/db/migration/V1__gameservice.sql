CREATE TABLE games (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE pieces (
    game_id TEXT NOT NULL,
    name TEXT NOT NULL,
    color TEXT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id),
    PRIMARY KEY (game_id, name, color)
);