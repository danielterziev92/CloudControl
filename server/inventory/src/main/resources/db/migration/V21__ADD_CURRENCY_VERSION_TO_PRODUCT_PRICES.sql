ALTER TABLE product_prices
    ADD currency_id BIGINT;

ALTER TABLE product_prices
    ADD version BIGINT;

ALTER TABLE product_prices
    ALTER COLUMN currency_id SET NOT NULL;

ALTER TABLE product_prices
    ADD CONSTRAINT FK_PRODUCT_PRICES_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currencies (id);

ALTER TABLE product_prices
    ALTER COLUMN price SET NOT NULL;
