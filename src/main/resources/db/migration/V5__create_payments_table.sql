CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_rent INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method ENUM('PIX', 'CREDIT_CARD', 'DEBIT_CARD', 'CASH') NOT NULL,
    status ENUM('PENDING', 'PAID', 'CANCELED', 'FAILED') NOT NULL,
    transaction_code VARCHAR(100),
    payment_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_rent
        FOREIGN KEY (id_rent)
        REFERENCES rent(id_rent)
        ON DELETE CASCADE
);
