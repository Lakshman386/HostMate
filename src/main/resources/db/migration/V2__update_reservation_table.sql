-- Add new columns to reservations table
ALTER TABLE reservations
ADD COLUMN IF NOT EXISTS guest_name VARCHAR(255) NOT NULL DEFAULT 'Guest',
ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20) NOT NULL DEFAULT '0000000000',
ADD COLUMN IF NOT EXISTS room_number INT NOT NULL DEFAULT 101,
ADD COLUMN IF NOT EXISTS payment_method VARCHAR(20) NULL,
ADD COLUMN IF NOT EXISTS payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
ADD COLUMN IF NOT EXISTS amount DOUBLE NOT NULL DEFAULT 0.0;

-- Rename total_amount to amount if it exists
ALTER TABLE reservations CHANGE COLUMN total_amount amount DOUBLE NOT NULL DEFAULT 0.0;

-- Add default values for existing reservations
UPDATE reservations r
JOIN guests g ON r.guest_id = g.id
JOIN rooms rm ON r.room_id = rm.id
SET r.guest_name = g.name,
    r.phone_number = g.phone_number,
    r.room_number = rm.room_number;
