-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS venta_galletas;
USE venta_galletas;

-- Tabla de Usuario (administradores y dependientes)
CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'dependiente') NOT NULL,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);

-- Tabla de Cliente
CREATE TABLE IF NOT EXISTS Cliente (
    id_cliente VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(20)
);

-- Tabla de Direccion (para los clientes)
CREATE TABLE IF NOT EXISTS Direccion (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente VARCHAR(15),
    provincia VARCHAR(100) NOT NULL,
    canton VARCHAR(100) NOT NULL,
    distrito VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    tipo ENUM('principal', 'alternativa') NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE
);

-- Tabla de Producto
CREATE TABLE IF NOT EXISTS Producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL
);

-- Tabla de Fotos de los productos
CREATE TABLE IF NOT EXISTS FotoProducto (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    foto VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE
);

-- Tabla de Orden
CREATE TABLE IF NOT EXISTS Orden (
    id_orden INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente VARCHAR(15),
    id_usuario VARCHAR(15) DEFAULT NULL,
    estado ENUM('pendiente', 'en_proceso', 'entregado', 'cancelado') DEFAULT 'pendiente',
    total DECIMAL(10, 2) NOT NULL,
    medio_pago VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE SET NULL
);

-- Tabla de Detalle de Orden
CREATE TABLE IF NOT EXISTS DetalleOrden (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    id_producto INT,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_orden) REFERENCES Orden(id_orden) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE
);
