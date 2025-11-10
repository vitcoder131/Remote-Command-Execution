
-- Xóa bảng cũ nếu có (để tránh lỗi khi chạy lại)
IF OBJECT_ID('CommandHistory', 'U') IS NOT NULL DROP TABLE CommandHistory;
IF OBJECT_ID('ServerConfig', 'U') IS NOT NULL DROP TABLE ServerConfig;
IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users;
GO
BEGIN
CREATE DATABASE RCE;
END
GO

USE RCE;
GO
-- Bảng Users
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    full_name NVARCHAR(100),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    role NVARCHAR(20) CHECK (role IN ('Admin', 'User')) DEFAULT 'User',
    status NVARCHAR(20) CHECK (status IN ('Active', 'Disabled')) DEFAULT 'Active',
    created_at DATETIME2 DEFAULT SYSDATETIME()
);
GO

-- Bảng CommandHistory
CREATE TABLE CommandHistory (
    history_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    user_ip NVARCHAR(50),
    server_ip NVARCHAR(50),
    command NVARCHAR(255),
    status NVARCHAR(50),
    time_ms INT,
    executed_at DATETIME2 DEFAULT SYSDATETIME(),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
GO

-- Bảng ServerConfig 
CREATE TABLE ServerConfig (
    config_id INT IDENTITY(1,1) PRIMARY KEY,
    server_name NVARCHAR(100),
    ip_address NVARCHAR(50),
    os_version NVARCHAR(50)
);
GO

-- ===== Thêm dữ liệu mẫu =====
INSERT INTO Users (username, full_name, email, phone, role, status)
VALUES 
('admin', N'Administrator', 'admin@remotecontrol.com', '0901001001', 'Admin', 'Active'),
('user1', N'Nguyễn Văn A', 'nguyenvana@email.com', '0902002002', 'User', 'Active'),
('user2', N'Trần Thị B', 'tranthib@email.com', '0903003003', 'User', 'Active'),
('testuser', N'Test User', 'test@email.com', '0904004004', 'User', 'Active'),
('disabled_user', N'User Đã Khóa', 'disabled@email.com', '0905005005', 'User', 'Disabled');
GO

INSERT INTO CommandHistory (user_id, user_ip, server_ip, command, status, time_ms)
VALUES
(2, '192.168.1.101', '192.168.1.100', 'hostname', 'Success', 120),
(3, '192.168.1.102', '192.168.1.100', 'ping -n 100 google.com', 'Timeout', 30000),
(3, '192.168.1.102', '192.168.1.100', 'netstat -an', 'Success', 650),
(2, '192.168.1.101', '192.168.1.100', 'tasklist', 'Success', 890),
(2, '192.168.1.101', '192.168.1.100', 'ping ooogle.com', 'Success', 4500);
GO

INSERT INTO ServerConfig (server_name, ip_address, os_version)
VALUES
('ServerMain', '192.168.1.100', 'Windows Server 2022'),
('BackupServer', '192.168.1.101', 'Windows Server 2019'),
('TestServer', '192.168.1.102', 'Ubuntu 22.04');
GO
