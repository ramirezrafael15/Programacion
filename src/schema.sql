CREATE DATABASE IF NOT EXISTS concesionario;
USE concesionario;

CREATE TABLE IF NOT EXISTS coches (
    id    INT         NOT NULL AUTO_INCREMENT,
    marca VARCHAR(100) NOT NULL,
    anio  INT          NOT NULL,
    color VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id)
);
