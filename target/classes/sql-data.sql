INSERT INTO `ims`.`customers` (`first_name`, `surname`)
VALUES ('jordan', 'harrison');
INSERT INTO `ims`.`item` (`name`, `price`)
VALUES ('test_item', 22.22);
INSERT INTO `ims`.`order` (`custid`)
values (1);
INSERT INTO `ims`.`order_link` (`orderid`, itemid, quantity)
values (1, 1, 1);