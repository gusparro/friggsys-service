INSERT INTO users (name, email, telephone, password_hash)
VALUES ('Gustavo F. Parro', 'gustavo.parro@email.com', '(63) 99969-4486',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
       ('João Silva', 'joao.silva@email.com', '(11) 98765-4321',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
       ('Maria Santos', 'maria.santos@email.com', '(19) 98765-4321',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
       ('Pedro Oliveira', 'pedro.oliveira@email.com', '(32) 98482-2390',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
       ('Ana Costa', 'ana.costa@email.com', '(65) 98438-7890',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (email) DO NOTHING;