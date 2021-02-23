package com.qa.ims.controller;

import com.qa.ims.exceptions.CustomerNotFoundException;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class OrderController implements CrudController<Order> {

    public static final Logger LOGGER = LogManager.getLogger();

    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private ItemDAO itemDAO;
    private Utils utils;

    public OrderController(CustomerDAO customerDAO, OrderDAO orderDAO, ItemDAO itemDAO, Utils utils) {
        this.customerDAO = customerDAO;
        this.itemDAO = itemDAO;
        this.orderDAO = orderDAO;
        this.utils = utils;
    }

    @Override
    public List<Order> readAll() {
        List<Order> orders = orderDAO.readAll();
        for (Order o : orders) {
            LOGGER.info(o);
        }
        return orders;
    }

    @Override
    public Order create() {
        LOGGER.info("Please enter the id of the customer you wish to create a new order for");
        Long custid = utils.getLong();
        Customer c;
        try {
            c = customerDAO.read(custid);
            Order order = orderDAO.create(new Order(c, new HashMap<>()));
            LOGGER.info("Order created");
            return order;
        } catch (CustomerNotFoundException e) {
            LOGGER.info("Customer with id " + custid + "not found");
            return null;
        }
    }

    @Override
    public Order update() {
        return null;
    }

    @Override
    public int delete() {
        return 0;
    }
}
