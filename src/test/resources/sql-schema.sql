DROP TABLE IF EXISTS `customers`;

CREATE TABLE IF NOT EXISTS `customers`
(
    `id`         INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) DEFAULT NULL,
    `surname`    VARCHAR(40) DEFAULT NULL,
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `item`;

CREATE TABLE IF NOT EXISTS `item`
(
    `iditem` INT         NOT NULL AUTO_INCREMENT,
    `name`   VARCHAR(45) NOT NULL,
    `price`  DOUBLE      NOT NULL,
    PRIMARY KEY (`iditem`)
);

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order`
(
    `idorder` INT NOT NULL AUTO_INCREMENT,
    `custid`  INT NOT NULL,
    PRIMARY KEY (`idorder`)
);

DROP TABLE IF EXISTS `order_link`;

CREATE TABLE `order_link`
(
    `idorder_link` INT NOT NULL AUTO_INCREMENT,
    `orderid`      INT NOT NULL,
    `itemid`       INT NOT NULL,
    `quantity`     INT NOT NULL,
    PRIMARY KEY (`idorder_link`)
);