INSERT INTO customers (`first_name`, `surname`)
VALUES ('jordan', 'harrison');

INSERT INTO `customers` (`first_name`, `surname`)
VALUES ('bob', 'bobson');

INSERT INTO `item` (`name`, `price`)
VALUES ('test_item', 22.22);

INSERT INTO `item` (`name`, `price`)
VALUES ('item', 22.26);

INSERT INTO `order` (`custid`)
values (1);

INSERT INTO `order_link` (`orderid`, itemid, quantity)
values (1, 1, 1);