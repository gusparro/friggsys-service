CREATE TABLE users
(
    id            UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    name          VARCHAR(100)             NOT NULL,
    telephone     VARCHAR(16)              NOT NULL,
    email         VARCHAR(100)             NOT NULL UNIQUE,
    password_hash VARCHAR(255)             NOT NULL,
    status        VARCHAR(20)              NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT check_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED')),
    CONSTRAINT check_name_not_empty CHECK (name != ''),
    CONSTRAINT check_email_not_empty CHECK (email != ''),
    CONSTRAINT check_telephone_not_empty CHECK (telephone != ''),
    CONSTRAINT check_telephone_length CHECK (length(telephone) <= 16),
    CONSTRAINT check_password_hash_not_empty CHECK (password_hash != '')
);