-- Drop all existing users except root
DELETE FROM mysql.user WHERE User != 'root';

-- Create application user
CREATE USER 'user'@'%' IDENTIFIED BY 'password';
CREATE DATABASE IF NOT EXISTS mydatabase;
GRANT ALL PRIVILEGES ON mydatabase.* TO 'user'@'%';

-- Apply changes immediately
FLUSH PRIVILEGES;
