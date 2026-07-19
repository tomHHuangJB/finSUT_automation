ALTER TABLE accounts
    ALTER COLUMN base_currency TYPE VARCHAR(3);

ALTER TABLE securities
    ALTER COLUMN currency TYPE VARCHAR(3);
