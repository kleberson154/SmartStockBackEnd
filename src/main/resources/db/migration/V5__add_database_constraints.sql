ALTER TABLE products
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE movements
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE products
    ADD CONSTRAINT chk_products_price_non_negative
        CHECK (price >= 0),
    ADD CONSTRAINT chk_products_quantity_non_negative
        CHECK (quantity >= 0),
    ADD CONSTRAINT chk_products_minimum_stock_non_negative
        CHECK (minimum_stock >= 0);

ALTER TABLE movements
    ADD CONSTRAINT chk_movements_quantity_positive
        CHECK (quantity > 0);