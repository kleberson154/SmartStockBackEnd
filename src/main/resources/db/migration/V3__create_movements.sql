CREATE TABLE movements (
    id UUID PRIMARY KEY,
    quantity INTEGER NOT NULL,
    observation VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    product_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_movement_product
        FOREIGN KEY (product_id)
            REFERENCES products(id)
);