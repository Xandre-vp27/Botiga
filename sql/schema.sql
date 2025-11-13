/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  alumnet
 * Created: 13 de nov. 2025
 */

-- Crear base de dades
CREATE DATABASE IF NOT EXISTS botiga;
USE botiga;

-- Taula de clients
CREATE TABLE Clients (
id INT AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL,
correu VARCHAR(100) UNIQUE NOT NULL
);

-- Taula de productes
CREATE TABLE Productes (
id INT AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL,
preu DECIMAL(10,2) NOT NULL,
estoc INT NOT NULL
);

-- Taula de comandes
CREATE TABLE Comandes (
id INT AUTO_INCREMENT PRIMARY KEY,
client_id INT NOT NULL,
data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
total DECIMAL(10,2) DEFAULT 0,
FOREIGN KEY (client_id) REFERENCES Clients(id)
);

-- Taula de línies de comanda
CREATE TABLE LiniesComanda (
id INT AUTO_INCREMENT PRIMARY KEY,
comanda_id INT NOT NULL,
producte_id INT NOT NULL,
quantitat INT NOT NULL,
preuUnitari DECIMAL(10,2) NOT NULL,
FOREIGN KEY (comanda_id) REFERENCES Comandes(id),
FOREIGN KEY (producte_id) REFERENCES Productes(id)
);

-- Taula de descomptes (per producte)
CREATE TABLE Descomptes (
id INT AUTO_INCREMENT PRIMARY KEY,
producte_id INT NOT NULL,
tipus ENUM('%','€') NOT NULL,
valor DECIMAL(10,2) NOT NULL,
FOREIGN KEY (producte_id) REFERENCES Productes(id)
);

-- Inserció de dades de prova
INSERT INTO Clients (nom, correu) VALUES
('Anna', 'anna@example.com'),
('Pere', 'pere@example.com');
INSERT INTO Productes (nom, preu, estoc) VALUES
('Ratolí', 19.99, 20),
('Teclat', 29.99, 15),
('Pantalla', 120.00, 10);
INSERT INTO Descomptes (producte_id, tipus, valor) VALUES
(1, '%', 10), -- 10% de descompte en Ratolí
(3, '€', 20); -- 20€ de descompte en Pantalla