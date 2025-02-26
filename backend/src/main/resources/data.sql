--USERS
INSERT INTO users (id, email, first_name, last_name, password) VALUES
('a1b2c3d4-e5f6-4789-90ab-cdef01234567', 'admin@example.com', 'James', 'Bond', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'), -- Password: 1234
('b8c9d0e1-f2a3-4b5c-6d7e-8f9a01234567', 'user@example.com', 'Tyler', 'Durden', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6') -- Password: 1234
ON CONFLICT DO NOTHING;

--ROLES
INSERT INTO role(id, name) VALUES
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'ADMIN'),
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'USER')
ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name) VALUES
('e9f0a1b2-c3d4-4e5f-6a7b-8c9d0e1f2a3b', 'USER_MODIFY_OWN'),
('f6a7b8c9-d0e1-4f2a-3b4c-d5e6f7a8b9c0', 'USER_DELETE_OWN'),
('a3b4c5d6-e7f8-490a-b1c2-d3e4f5a6b7c8', 'USER_MODIFY_ALL'),
('b0c1d2e3-f4a5-46b7-c8d9-e0f1a2b3c4d5', 'USER_DELETE_ALL'),
('c7d8e9f0-a1b2-4c3d-e4f5-a6b7c8d9e0f1', 'MYLISTENTRY_MODIFY_OWN'),
('d4e5f6a7-b8c9-4d0e-f1a2-b3c4d5e6f7a8', 'MYLISTENTRY_DELETE_OWN'),
('e1f2a3b4-c5d6-4e7f-8a9b-c0d1e2f3a4b5', 'MYLISTENTRY_MODIFY_ALL'),
('f8a9b0c1-d2e3-4f5a-6b7c-8d9e0f1a2b3c', 'MYLISTENTRY_DELETE_ALL'),
('9ab0c1d2-e3f4-45a6-b7c8-d9e0f1a2b3c4', 'USER_READ_ALL'),
('a5b6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c0', 'MYLISTENTRY_READ_ALL')
ON CONFLICT DO NOTHING;

--assign roles to users
INSERT INTO users_role (users_id, role_id) VALUES
('a1b2c3d4-e5f6-4789-90ab-cdef01234567', 'c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0'),
('a1b2c3d4-e5f6-4789-90ab-cdef01234567', 'd2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6'),
('b8c9d0e1-f2a3-4b5c-6d7e-8f9a01234567', 'd2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6')
ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id) VALUES
-- ADMIN role: All authorities
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'a3b4c5d6-e7f8-490a-b1c2-d3e4f5a6b7c8'), -- USER_MODIFY_ALL
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'b0c1d2e3-f4a5-46b7-c8d9-e0f1a2b3c4d5'), -- USER_DELETE_ALL
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'e1f2a3b4-c5d6-4e7f-8a9b-c0d1e2f3a4b5'), -- MYLISTENTRY_MODIFY_ALL
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'f8a9b0c1-d2e3-4f5a-6b7c-8d9e0f1a2b3c'), -- MYLISTENTRY_DELETE_ALL
--Include own authorities as well
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'e9f0a1b2-c3d4-4e5f-6a7b-8c9d0e1f2a3b'), -- USER_MODIFY_OWN
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'f6a7b8c9-d0e1-4f2a-3b4c-d5e6f7a8b9c0'), -- USER_DELETE_OWN
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'c7d8e9f0-a1b2-4c3d-e4f5-a6b7c8d9e0f1'), -- MYLISTENTRY_MODIFY_OWN
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'd4e5f6a7-b8c9-4d0e-f1a2-b3c4d5e6f7a8'), -- MYLISTENTRY_DELETE_OWN
-- USER role: Own authorities only
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'e9f0a1b2-c3d4-4e5f-6a7b-8c9d0e1f2a3b'), -- USER_MODIFY_OWN
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'f6a7b8c9-d0e1-4f2a-3b4c-d5e6f7a8b9c0'), -- USER_DELETE_OWN
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'c7d8e9f0-a1b2-4c3d-e4f5-a6b7c8d9e0f1'), -- MYLISTENTRY_MODIFY_OWN
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'd4e5f6a7-b8c9-4d0e-f1a2-b3c4d5e6f7a8'), -- MYLISTENTRY_DELETE_OWN
-- New READ authorities for both roles
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', '9ab0c1d2-e3f4-45a6-b7c8-d9e0f1a2b3c4'), -- USER_READ_ALL for ADMIN
('c5d6e7f8-90a1-4b2c-d3e4-f5a6b7c8d9e0', 'a5b6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c0'), -- MYLISTENTRY_READ_ALL for ADMIN
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', '9ab0c1d2-e3f4-45a6-b7c8-d9e0f1a2b3c4'), -- USER_READ_ALL for USER
('d2e3f4a5-b6c7-4d8e-f9a0-b1c2d3e4f5a6', 'a5b6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c0')  -- MYLISTENTRY_READ_ALL for USER
ON CONFLICT DO NOTHING;