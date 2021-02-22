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

CREATE TABLE `ims`.`order`
(
    `idorder` INT NOT NULL AUTO_INCREMENT,
    `custid`  INT NOT NULL,
    PRIMARY KEY (`idorder`)
);

CREATE TABLE `ims`.`order_link`
(
    `idorder_link` INT NOT NULL AUTO_INCREMENT,
    `orderid`      INT NOT NULL,
    `itemid`       INT NOT NULL,
    `quantity`     INT NOT NULL,
    PRIMARY KEY (`idorder_link`)
);