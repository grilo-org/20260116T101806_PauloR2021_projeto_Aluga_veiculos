CREATE TABLE vehicles (
    id_vehicles INT AUTO_INCREMENT PRIMARY KEY,
    mark VARCHAR(100),
    model VARCHAR(100) NOT NULL,
    plate VARCHAR(10) UNIQUE NOT NULL,
    year_vehicle INT,
    daily_value DECIMAL(10,2),
    available BOOLEAN DEFAULT TRUE
);