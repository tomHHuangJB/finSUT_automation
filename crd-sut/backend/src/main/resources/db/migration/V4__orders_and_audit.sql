CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    external_order_id VARCHAR(80) NOT NULL UNIQUE,
    account_id UUID NOT NULL REFERENCES accounts(id),
    security_id UUID NOT NULL REFERENCES securities(id),
    side VARCHAR(10) NOT NULL,
    order_type VARCHAR(20) NOT NULL,
    quantity NUMERIC(19, 4) NOT NULL,
    limit_price NUMERIC(19, 6),
    filled_quantity NUMERIC(19, 4) NOT NULL DEFAULT 0,
    remaining_quantity NUMERIC(19, 4) NOT NULL,
    average_execution_price NUMERIC(19, 6),
    status VARCHAR(40) NOT NULL,
    created_by VARCHAR(80) NOT NULL,
    approved_by VARCHAR(80),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    approved_at TIMESTAMPTZ,
    released_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT chk_order_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_order_limit_price_nonnegative CHECK (limit_price IS NULL OR limit_price >= 0),
    CONSTRAINT chk_order_filled_nonnegative CHECK (filled_quantity >= 0),
    CONSTRAINT chk_order_remaining_nonnegative CHECK (remaining_quantity >= 0),
    CONSTRAINT chk_order_filled_not_over_quantity CHECK (filled_quantity <= quantity)
);

CREATE INDEX idx_orders_account_id ON orders(account_id);
CREATE INDEX idx_orders_security_id ON orders(security_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

CREATE TABLE audit_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(80) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(80) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    performed_by VARCHAR(80) NOT NULL,
    correlation_id VARCHAR(80) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_events_entity ON audit_events(entity_type, entity_id);
CREATE INDEX idx_audit_events_correlation_id ON audit_events(correlation_id);
