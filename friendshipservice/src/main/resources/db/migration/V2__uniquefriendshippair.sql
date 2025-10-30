-- Creo una condición para que no se pueda repetir una relación de amistad
CREATE UNIQUE INDEX unique_friendship_pair
ON friendships (LEAST(user_id_1, user_id_2), GREATEST(user_id_1, user_id_2));