DROP TABLE IF EXISTS friendships CASCADE;

CREATE TABLE friendships (
    user_id_1 UUID,
    user_id_2 UUID,
    PRIMARY KEY (user_id_1, user_id_2)
)
