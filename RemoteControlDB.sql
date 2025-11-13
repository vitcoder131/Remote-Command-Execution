IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'RemoteControlDB')
BEGIN
    CREATE DATABASE RemoteControlDB;
END
GO

USE RemoteControlDB;
GO

-- DROP theo thứ tự: Bảng con trước, bảng cha sau
IF OBJECT_ID('dbo.CommandHistory', 'U') IS NOT NULL DROP TABLE dbo.CommandHistory;
IF OBJECT_ID('dbo.ServerConfig', 'U') IS NOT NULL DROP TABLE dbo.ServerConfig;
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
GO

CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL UNIQUE,
    password_hash NVARCHAR(500) NOT NULL,
    full_name NVARCHAR(100),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    is_admin BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT CHK_Users_Username_Length CHECK (LEN(username) >= 3)
);

CREATE INDEX idx_Users_username ON Users(username);
CREATE INDEX idx_Users_email ON Users(email);
GO

CREATE TABLE ServerConfig (
    server_id INT PRIMARY KEY IDENTITY(1,1),
    server_name NVARCHAR(100) NOT NULL,
    server_ip NVARCHAR(50) NOT NULL,
    server_port INT DEFAULT 9999,
    max_connections INT DEFAULT 10,
    CONSTRAINT CHK_ServerConfig_Port CHECK (server_port BETWEEN 1024 AND 65535),
    CONSTRAINT UQ_ServerConfig_IP_Port UNIQUE (server_ip, server_port)
);
GO

CREATE TABLE CommandHistory (
    history_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    server_id INT,
    server_ip NVARCHAR(50) NOT NULL,
    command NVARCHAR(MAX) NOT NULL,
    result NVARCHAR(MAX),
    error_message NVARCHAR(MAX),
    client_ip NVARCHAR(50),
    CONSTRAINT FK_CommandHistory_user_id 
        FOREIGN KEY (user_id) 
        REFERENCES Users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_CommandHistory_server_id 
        FOREIGN KEY (server_id) 
        REFERENCES ServerConfig(server_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE INDEX idx_CommandHistory_user_id ON CommandHistory(user_id);
CREATE INDEX idx_CommandHistory_server_id ON CommandHistory(server_id);
CREATE INDEX idx_CommandHistory_server_ip ON CommandHistory(server_ip);
GO

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'RemoteControlDB')
BEGIN
    CREATE DATABASE RemoteControlDB;
END
GO

USE RemoteControlDB;
GO

-- DROP theo thứ tự: Bảng con trước, bảng cha sau
IF OBJECT_ID('dbo.CommandHistory', 'U') IS NOT NULL DROP TABLE dbo.CommandHistory;
IF OBJECT_ID('dbo.ServerConfig', 'U') IS NOT NULL DROP TABLE dbo.ServerConfig;
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
GO

CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL UNIQUE,
    password_hash NVARCHAR(500) NOT NULL,
    full_name NVARCHAR(100),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    is_admin BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT CHK_Users_Username_Length CHECK (LEN(username) >= 3)
);

CREATE INDEX idx_Users_username ON Users(username);
CREATE INDEX idx_Users_email ON Users(email);
GO

CREATE TABLE ServerConfig (
    server_id INT PRIMARY KEY IDENTITY(1,1),
    server_name NVARCHAR(100) NOT NULL,
    server_ip NVARCHAR(50) NOT NULL,
    server_port INT DEFAULT 9999,
    max_connections INT DEFAULT 10,
    CONSTRAINT CHK_ServerConfig_Port CHECK (server_port BETWEEN 1024 AND 65535),
    CONSTRAINT UQ_ServerConfig_IP_Port UNIQUE (server_ip, server_port)
);
GO

CREATE TABLE CommandHistory (
    history_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    server_id INT,
    server_ip NVARCHAR(50) NOT NULL,
    command NVARCHAR(MAX) NOT NULL,
    result NVARCHAR(MAX),
    error_message NVARCHAR(MAX),
    client_ip NVARCHAR(50),
    CONSTRAINT FK_CommandHistory_user_id 
        FOREIGN KEY (user_id) 
        REFERENCES Users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_CommandHistory_server_id 
        FOREIGN KEY (server_id) 
        REFERENCES ServerConfig(server_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE INDEX idx_CommandHistory_user_id ON CommandHistory(user_id);
CREATE INDEX idx_CommandHistory_server_id ON CommandHistory(server_id);
CREATE INDEX idx_CommandHistory_server_ip ON CommandHistory(server_ip);
GO

USE RemoteControlDB;
GO

-- Thêm người dùng quản trị
INSERT INTO Users (username, password_hash, full_name, email, phone, is_admin)
VALUES 
(N'admin', CONVERT(NVARCHAR(500), HASHBYTES('SHA2_256', 'admin123'), 2), N'Quản trị viên', N'admin@remote.com', N'0909123456', 1),

-- Thêm người dùng thường
(N'bao', CONVERT(NVARCHAR(500), HASHBYTES('SHA2_256', 'bao123'), 2), N'Đinh Quốc Bảo', N'bao@example.com', N'0987654321', 0),
(N'guest', CONVERT(NVARCHAR(500), HASHBYTES('SHA2_256', 'guest123'), 2), N'Khách truy cập', N'guest@example.com', N'0911222333', 0);
GO

INSERT INTO ServerConfig (server_name, server_ip, server_port, max_connections)
VALUES
(N'Máy chủ chính', N'192.168.1.10', 8080, 50),
(N'Máy chủ dự phòng', N'192.168.1.11', 8081, 20),
(N'Máy chủ thử nghiệm', N'127.0.0.1', 9999, 5);
GO

INSERT INTO CommandHistory (user_id, server_id, server_ip, command, result, client_ip)
VALUES
(1, 1, N'192.168.1.10', N'dir', N'Danh sách thư mục: Documents, Downloads, Desktop', N'192.168.1.5'),
(2, 3, N'127.0.0.1', N'ipconfig', N'IPv4 Address: 127.0.0.1', N'192.168.1.22'),
(3, 2, N'192.168.1.11', N'ping google.com', N'Kết quả: 4 gói tin thành công', N'10.0.0.15');
GO
