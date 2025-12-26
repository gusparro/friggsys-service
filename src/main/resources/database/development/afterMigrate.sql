INSERT INTO users (name, email, telephone, password_hash)
VALUES ('Gustavo F. Parro', 'gustavo.parro@email.com', '(63) 99969-4486',
        '$2a$10$MATzYn25717MrQTCT2oR1uwIbGZoDwuXcfBykyzuVBjebYh0.sIOy'),
       ('João Silva', 'joao.silva@email.com', '(11) 98765-4321',
        '$2a$10$MATzYn25717MrQTCT2oR1uwIbGZoDwuXcfBykyzuVBjebYh0.sIOy'),
       ('Maria Santos', 'maria.santos@email.com', '(19) 98765-4321',
        '$2a$10$MATzYn25717MrQTCT2oR1uwIbGZoDwuXcfBykyzuVBjebYh0.sIOy'),
       ('Pedro Oliveira', 'pedro.oliveira@email.com', '(32) 98482-2390',
        '$2a$10$MATzYn25717MrQTCT2oR1uwIbGZoDwuXcfBykyzuVBjebYh0.sIOy'),
       ('Ana Costa', 'ana.costa@email.com', '(65) 98438-7890',
        '$2a$10$MATzYn25717MrQTCT2oR1uwIbGZoDwuXcfBykyzuVBjebYh0.sIOy')
ON CONFLICT (email) DO NOTHING;