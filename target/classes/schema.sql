-- Room Table
CREATE TABLE IF NOT EXISTS room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_number INT NOT NULL UNIQUE,
    room_type VARCHAR(20) NOT NULL,
    rate_per_night DECIMAL(10,2) NOT NULL,
    floor_number INT,
    max_occupancy INT,
    description TEXT,
    amenities VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

-- Guest Table
CREATE TABLE IF NOT EXISTS guest (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT,
    loyalty_points INT DEFAULT 0,
    membership_level VARCHAR(20) DEFAULT 'STANDARD',
    special_preferences TEXT,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Reservation Table
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT NOT NULL,  -- References guest table
    room_id BIGINT NOT NULL,   -- References room table
    reservation_date DATETIME DEFAULT CURRENT_TIMESTAMP,  -- Automatically stores reservation time
    check_in_date DATETIME NOT NULL,
    check_out_date DATETIME NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(20),
    special_requests TEXT,
    FOREIGN KEY (guest_id) REFERENCES guest(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

