--USERS
INSERT INTO users (id, email, first_name, last_name, password)
VALUES ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James', 'Bond',
        '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'), -- Password: 1234
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler', 'Durden',
        '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6') -- Password: 1234
ON CONFLICT DO NOTHING;


--ROLES
INSERT INTO role(id, name)
VALUES ('ab505c92-7280-49fd-a7de-258e618df074', 'ADMIN'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER')
ON CONFLICT DO NOTHING;


--AUTHORITIES
INSERT INTO authority(id, name)
VALUES ('76d2cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_MODIFY_OWN'),  
       ('21c942db-a275-43f8-bdd6-d048c21bf5ab', 'USER_DELETE_OWN'),  
       ('87e3cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_MODIFY_ALL'),  
       ('32d42dbf-a275-43f8-bdd6-d048c21bf5ab', 'USER_DELETE_ALL'),  
       ('3a1f5b6e-8c3b-4d6b-9b8e-2a8b3e1d5f6a', 'MYLISTENTRY_MODIFY_OWN'),
       ('4b2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b', 'MYLISTENTRY_DELETE_OWN'),
       ('5c2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b', 'MYLISTENTRY_MODIFY_ALL'),
       ('6d2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b', 'MYLISTENTRY_DELETE_ALL')
ON CONFLICT DO NOTHING;

--assign roles to users
INSERT INTO users_role (users_id, role_id)
VALUES ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'ab505c92-7280-49fd-a7de-258e618df074'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02') 
ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES
    -- ADMIN role:  All authorities
    ('ab505c92-7280-49fd-a7de-258e618df074', '87e3cbf6-5845-470e-ad5f-2edb9e09a868'), -- USER_MODIFY_ALL
    ('ab505c92-7280-49fd-a7de-258e618df074', '32d42dbf-a275-43f8-bdd6-d048c21bf5ab'), -- USER_DELETE_ALL
    ('ab505c92-7280-49fd-a7de-258e618df074', '5c2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b'), -- MYLISTENTRY_MODIFY_ALL
    ('ab505c92-7280-49fd-a7de-258e618df074', '6d2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b'), -- MYLISTENTRY_DELETE_ALL
     --Include own authorities as well
    ('ab505c92-7280-49fd-a7de-258e618df074', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'), -- USER_MODIFY_OWN
    ('ab505c92-7280-49fd-a7de-258e618df074', '21c942db-a275-43f8-bdd6-d048c21bf5ab'), -- USER_DELETE_OWN
    ('ab505c92-7280-49fd-a7de-258e618df074', '3a1f5b6e-8c3b-4d6b-9b8e-2a8b3e1d5f6a'), -- MYLISTENTRY_MODIFY_OWN
    ('ab505c92-7280-49fd-a7de-258e618df074', '4b2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b'), -- MYLISTENTRY_DELETE_OWN

    -- USER role: Own authorities only
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'), -- USER_MODIFY_OWN
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '21c942db-a275-43f8-bdd6-d048c21bf5ab'), -- USER_DELETE_OWN
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '3a1f5b6e-8c3b-4d6b-9b8e-2a8b3e1d5f6a'), -- MYLISTENTRY_MODIFY_OWN
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '4b2f6c7e-9d4c-4e7b-8b9e-3b9c4e2d6f7b')  -- MYLISTENTRY_DELETE_OWN
ON CONFLICT DO NOTHING;