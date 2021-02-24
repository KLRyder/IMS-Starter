package com.qa.ims.controller;

import com.qa.ims.exceptions.CustomerNotFoundException;
import com.qa.ims.exceptions.ItemNotFoundException;
import com.qa.ims.exceptions.OrderNotFoundException;
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

    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;
    private final ItemDAO itemDAO;
    private final Utils utils;

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
        LOGGER.info("Please enter the id of the order that you would like to update");
        Long id = utils.getLong();
        try {
            Order orderToUpdate = orderDAO.read(id);
            UpdateOrderAction action;
            do {
                LOGGER.info("Order is currently as follows: \n" + orderToUpdate);
                LOGGER.info("What would you like to do with this order?");
                UpdateOrderAction.printActions();


                action = UpdateOrderAction.getAction(utils);
                if (action == UpdateOrderAction.CANCEL){
                    LOGGER.info("Update cancelled");
                    return null;
                }
                updateOrder(action, orderToUpdate);
            } while (action != UpdateOrderAction.DONE);
            return orderDAO.update(orderToUpdate);
        } catch (OrderNotFoundException e) {
            LOGGER.info("No order with ID " + id + " can be found. Please try again.");
        }
        return null;
    }

    private void updateOrder(UpdateOrderAction action, Order orderToUpdate) {
        Long id;
        switch (action) {
            case ADD:
                LOGGER.info("Enter the ID of the item you wish to add");
                id = utils.getLong();
                if (orderToUpdate.containsItem(id)) {
                    LOGGER.info("Order already contains an item with ID " + id +
                            ". Did you mean to change the QUANTITY instead?");
                    break;
                }
                try {
                    Item toAdd = itemDAO.read(id);
                    LOGGER.info("How many units of " + toAdd.getName() + " do you wish to add?");
                    int quant = utils.getInt();
                    if (quant < 1) {
                        LOGGER.info("You need to add at least one item. If you wish to remove items try using REMOVE.");
                        break;
                    }
                    orderToUpdate.addItem(toAdd, quant);
                } catch (ItemNotFoundException e) {
                    LOGGER.info("No item with ID " + id + " can be found. Please try again.");
                }
                break;

            case REMOVE:
                LOGGER.info("Enter the ID of the item you wish to remove:");
                id = utils.getLong();
                if (!orderToUpdate.removeItem(id)) {
                    LOGGER.info("Order does not contain an item with ID " + id +
                            ". Did you mean to ADD an item instead?");
                }
                break;

            case QUANTITY:
                LOGGER.info("Enter the ID of the item whose quantity you wish to change:");
                id = utils.getLong();
                if (!orderToUpdate.containsItem(id)) {
                    LOGGER.info("No item with ID " + id + " can be found. Did you mean to ADD the item instead?");
                    break;
                }
                LOGGER.info("Order currently has " + orderToUpdate.getItemQuantity(id) +
                        " of item ID " + id + ".\nHow enter new quantity:");
                int quant = utils.getInt();
                if (quant < 1) {
                    LOGGER.info("Orders cannot contain less than one of an item. Removing item instead");
                    orderToUpdate.removeItem(id);
                }
                orderToUpdate.setItemQuantity(id, quant);
                break;

            case CUSTOMER:
                LOGGER.info("Enter the ID of the customer you wish to change to:");
                id = utils.getLong();
                try {
                    orderToUpdate.setCustomer(customerDAO.read(id));
                    break;
                } catch (CustomerNotFoundException e) {
                    LOGGER.info("No customer with ID " + id + " can be found. Please try again.");
                }
                break;

            case DONE:
                LOGGER.info("Updating order...");
                break;
        }
    }

    //TODO make the user confirm that they wish to delete the order. Potently read order to confirm first?
    @Override
    public int delete() {
        LOGGER.info("Please enter the id  of the order you want to delete");
        Long id = utils.getLong();
        return orderDAO.delete(id);
    }
}
