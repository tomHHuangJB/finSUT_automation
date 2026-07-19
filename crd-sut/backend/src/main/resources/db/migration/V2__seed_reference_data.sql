INSERT INTO app_users (username, password_hash, display_name, role, active)
VALUES
  ('pm_user', '{noop}password', 'Portfolio Manager User', 'PORTFOLIO_MANAGER', true),
  ('compliance_user', '{noop}password', 'Compliance User', 'COMPLIANCE_OFFICER', true),
  ('trader_user', '{noop}password', 'Trader User', 'TRADER', true),
  ('operations_user', '{noop}password', 'Operations User', 'OPERATIONS', true),
  ('admin_user', '{noop}password', 'Admin User', 'ADMIN', true),
  ('readonly_user', '{noop}password', 'Read Only User', 'READ_ONLY', true);

INSERT INTO accounts (account_code, account_name, account_type, base_currency, status, cash_balance)
VALUES
  ('ACC-1001', 'Core Growth Account', 'INSTITUTIONAL', 'USD', 'ACTIVE', 1000000.0000),
  ('ACC-1002', 'Low Cash Account', 'INDIVIDUAL', 'USD', 'ACTIVE', 2500.0000),
  ('ACC-1003', 'Inactive Account', 'PENSION', 'USD', 'INACTIVE', 500000.0000),
  ('ACC-1004', 'No Positions Account', 'MUTUAL_FUND', 'USD', 'ACTIVE', 750000.0000),
  ('ACC-1005', 'Concentration Watch Account', 'HEDGE_FUND', 'USD', 'ACTIVE', 2000000.0000);

INSERT INTO securities (symbol, name, asset_class, currency, current_price, price_timestamp, restricted, active)
VALUES
  ('AAPL', 'Apple Inc.', 'EQUITY', 'USD', 210.000000, now(), false, true),
  ('MSFT', 'Microsoft Corporation', 'EQUITY', 'USD', 430.000000, now(), false, true),
  ('IBM', 'International Business Machines', 'EQUITY', 'USD', 190.000000, now(), false, true),
  ('RESTRICTED1', 'Restricted Test Security', 'EQUITY', 'USD', 50.000000, now(), true, true),
  ('STALE1', 'Stale Price Security', 'EQUITY', 'USD', 25.000000, now() - interval '10 days', false, true),
  ('INACTIVE1', 'Inactive Security', 'EQUITY', 'USD', 15.000000, now(), false, false),
  ('BOND1', 'Practice Corporate Bond', 'BOND', 'USD', 98.500000, now(), false, true),
  ('ETF1', 'Practice Equity ETF', 'ETF', 'USD', 120.000000, now(), false, true);
