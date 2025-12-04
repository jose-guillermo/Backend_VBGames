-- Permite la creación de uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles VALUES(uuid_generate_v4(), 'ROLE_ADMIN');
INSERT INTO roles VALUES(uuid_generate_v4(), 'ROLE_USER');