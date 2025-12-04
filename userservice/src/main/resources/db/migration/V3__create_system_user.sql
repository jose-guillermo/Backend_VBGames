INSERT INTO users (id, username, email, created_at)
VALUES ('00000000-0000-0000-0000-000000000001', 'system', 'system@internal.local', (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint)
ON CONFLICT DO NOTHING;