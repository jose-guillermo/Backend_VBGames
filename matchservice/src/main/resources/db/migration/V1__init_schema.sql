DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS moves CASCADE;
DROP TABLE IF EXISTS favourite_matches CASCADE;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username TEXT UNIQUE
);

CREATE TABLE games (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE matches (
    id UUID PRIMARY KEY,
    player_1_id UUID NOT NULL,
    player_2_id UUID NOT NULL,
    winner_id UUID,
    game_id UUID NOT NULL,
    played_at BIGINT NOT NULL,
    asynchronous BOOLEAN NOT NULL,
    FOREIGN KEY (player_1_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (player_2_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (winner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE
);

CREATE TABLE moves (
    turn SMALLINT,
    match_id UUID,
    from_row SMALLINT NOT NULL,
    from_col SMALLINT NOT NULL,
    to_row SMALLINT NOT NULL,
    to_col SMALLINT NOT NULL,
    game_over BOOLEAN NOT NULL,
    PRIMARY KEY (turn, match_id),
    FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE
);

CREATE TABLE favourite_matches(
    match_id UUID,
    user_id UUID,
    PRIMARY KEY (user_id, match_id),
    FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE

);