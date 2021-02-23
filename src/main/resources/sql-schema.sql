drop schema ims;

CREATE SCHEMA IF NOT EXISTS `ims`;

USE `ims`;

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
    KEY `custid_idx` (`custid`),
    CONSTRAINT `custid` FOREIGN KEY (`custid`) REFERENCES `ims`.`customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `ims`.`order_link`
(
    `idorder_link` int NOT NULL AUTO_INCREMENT,
    `orderid`      int NOT NULL,
    `itemid`       int NOT NULL,
    `quantity`     int NOT NULL,
    PRIMARY KEY (`idorder_link`),
    KEY `orderid_idx` (`orderid`),
    KEY `itemid_idx` (`itemid`),
    CONSTRAINT `itemid` FOREIGN KEY (`itemid`) REFERENCES `ims`.`item` (`iditem`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orderid` FOREIGN KEY (`orderid`) REFERENCES `ims`.`order` (`idorder`) ON DELETE CASCADE ON UPDATE CASCADE
);