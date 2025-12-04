INSERT INTO users (id, username, password, email, verified)
VALUES ('00000000-0000-0000-0000-000000000001', 'system', '$2a$12$N9qo8uLOickgx2ZMRZo5i.e7W.X9DCnGJ7U6v4YjHG5u8rMG/60uW', 'system@internal.local', true)
ON CONFLICT DO NOTHING;