-- Tabla para los Clientes
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    rif VARCHAR(20),
    empresa VARCHAR(100),
    tipo_cobranza VARCHAR(50),
    inventario_preferido VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Tabla para los Proveedores
CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    rif VARCHAR(20),
    empresa VARCHAR(100),
    tipo_cobranza VARCHAR(50),
    inventario_preferido VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Tabla para los Empleados
CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    cargo VARCHAR(100),
    salario_base DECIMAL(10, 2), -- Guardará montos como 500.00
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Tabla para el Inventario
CREATE TABLE inventario (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    cantidad INTEGER NOT NULL DEFAULT 0,
    precio_unitario DECIMAL(10, 2),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
