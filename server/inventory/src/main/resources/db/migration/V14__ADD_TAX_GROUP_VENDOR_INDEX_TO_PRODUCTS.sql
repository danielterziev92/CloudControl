ALTER TABLE products
    ADD tax_group_id BIGINT;

ALTER TABLE products
    ADD vendor_id BIGINT;

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_TAX_GROUP FOREIGN KEY (tax_group_id) REFERENCES product_tax_groups (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_VENDOR FOREIGN KEY (vendor_id) REFERENCES product_vendors (id);

CREATE INDEX idx_products_vendor_id ON products (vendor_id);