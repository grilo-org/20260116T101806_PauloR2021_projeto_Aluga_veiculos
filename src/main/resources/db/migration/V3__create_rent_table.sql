CREATE TABLE rent (
    id_rent INT AUTO_INCREMENT PRIMARY KEY,

    id_user INT NOT NULL,
    id_vehicle INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    total_value DECIMAL(10,2),
    vehicle_removed BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_rent_user
        FOREIGN KEY (id_user)
        REFERENCES users(id),

    CONSTRAINT fk_rent_vehicle
        FOREIGN KEY (id_vehicle)
        REFERENCES vehicles(id_vehicles)
);