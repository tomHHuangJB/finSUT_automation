CREATE TABLE app_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    role VARCHAR(40) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_code VARCHAR(40) NOT NULL UNIQUE,
    account_name VARCHAR(160) NOT NULL,
    account_type VARCHAR(40) NOT NULL,
    base_currency CHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(40) NOT NULL,
    cash_balance NUMERIC(19, 4) NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE securities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    symbol VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(180) NOT NULL,
    asset_class VARCHAR(40) NOT NULL,
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    current_price NUMERIC(19, 6) NOT NULL,
    price_timestamp TIMESTAMPTZ NOT NULL,
    restricted BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
