package com.qa.ims.controller;

import java.util.List;

import com.qa.ims.exceptions.CustomerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.utils.Utils;

/**
 * Takes in customer details for CRUD functionality
 */
public class CustomerController implements CrudController<Customer> {

    public static final Logger LOGGER = LogManager.getLogger();

    private final CustomerDAO customerDAO;
    private final Utils utils;

    public CustomerController(CustomerDAO customerDAO, Utils utils) {
        super();
        this.customerDAO = customerDAO;
        this.utils = utils;
    }

    /**
     * Reads all customers to the logger
     */
    @Override
    public List<Customer> readAll() {
        List<Customer> customers = customerDAO.readAll();
        for (Customer customer : customers) {
            LOGGER.info(customer);
        }
        return customers;
    }

    /**
     * Creates a customer by taking in user input
     */
    @Override
    public Customer create() {
        LOGGER.info("Please enter a first name");
        String firstName = utils.getString();
        LOGGER.info("Please enter a surname");
        String surname = utils.getString();
        Customer customer = customerDAO.create(new Customer(firstName, surname));
        LOGGER.info("Customer created");
        return customer;
    }

    /**
     * Updates an existing customer by taking in user input
     */
    @Override
    public Customer update() {
        LOGGER.info("Please enter the id of the customer you would like to update");
        Long id = utils.getLong();
        LOGGER.info("Please enter a first name");
        String firstName = utils.getString();
        LOGGER.info("Please enter a surname");
        String surname = utils.getString();
        Customer customer = null;
        try {
            customer = customerDAO.update(new Customer(id, firstName, surname));
            LOGGER.info("Customer Updated");
        } catch (CustomerNotFoundException e) {
            LOGGER.info("Customer with ID " + id + " not found.");
        }
        return customer;
    }

    /**
     * Deletes an existing customer by the id of the customer
     *
     * @return
     */
    @Override
    public int delete() {
        LOGGER.info("Please enter the id of the customer you would like to delete");
        Long id = utils.getLong();
        return customerDAO.delete(id);
    }

}
