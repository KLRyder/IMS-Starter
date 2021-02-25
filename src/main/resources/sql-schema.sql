drop schema ims;

CREATE SCHEMA IF NOT EXISTS `ims`;

USE `ims`;

DROP TABLE IF EXISTS `order_link`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `customers`;

CREATE TABLE IF NOT EXISTS `ims`.`customers`
(
    `id`         INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) DEFAULT NULL,
    `surname`    VARCHAR(40) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `ims`.`item`
(
    `iditem` INT         NOT NULL AUTO_INCREMENT,
    `name`   VARCHAR(45) NOT NULL,
    `price`  DOUBLE      NOT NULL,
    PRIMARY KEY (`iditem`)
);

CREATE TABLE IF NOT EXISTS `ims`.`order`
(
    `idorder` int NOT NULL AUTO_INCREMENT,
    `custid`  int NOT NULL,
    PRIMARY KEY (`idorder`),
    FOREIGN KEY (`custid`) REFERENCES `ims`.`customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `ims`.`order_link`
(
    `idorder_link` int NOT NULL AUTO_INCREMENT,
    `orderid`      int NOT NULL,
    `itemid`       int NOT NULL,
    `quantity`     int NOT NULL,
    PRIMARY KEY (`idorder_link`),
    FOREIGN KEY (`itemid`) REFERENCES `ims`.`item` (`iditem`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`orderid`) REFERENCES `ims`.`order` (`idorder`) ON DELETE CASCADE ON UPDATE CASCADE
);