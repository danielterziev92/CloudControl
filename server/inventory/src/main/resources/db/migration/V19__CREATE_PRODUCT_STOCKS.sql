CREATE SEQUENCE IF NOT EXISTS product_stocks_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE product_stocks
(
    id         BIGINT         NOT NULL,
    version    BIGINT,
    quantity   DECIMAL(15, 8) NOT NULL,
    object_id  BIGINT         NOT NULL,
    lot_id     BIGINT,
    product_id BIGINT         NOT NULL,
    CONSTRAINT pk_product_stocks PRIMARY KEY (id)
);

ALTER TABLE product_stocks
    ADD CONSTRAINT uc_product_stock_product_object_lot UNIQUE (product_id, object_id, lot_id);

CREATE INDEX idx_product_stocks_product_object ON product_stocks (product_id, object_id);

CREATE INDEX idx_product_stocks_product_object_lot ON product_stocks (product_id, object_id, lot_id);

ALTER TABLE product_stocks
    ADD CONSTRAINT FK_PRODUCT_STOCKS_ON_LOT FOREIGN KEY (lot_id) REFERENCES product_lots (id);

CREATE INDEX idx_product_stocks_lot_id ON product_stocks (lot_id);

ALTER TABLE product_stocks
    ADD CONSTRAINT FK_PRODUCT_STOCKS_ON_OBJECT FOREIGN KEY (object_id) REFERENCES objects (id);

CREATE INDEX idx_product_stocks_object_id ON product_stocks (object_id);

ALTER TABLE product_stocks
    ADD CONSTRAINT FK_PRODUCT_STOCKS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

CREATE INDEX idx_product_stocks_product_id ON product_stocks (product_id);