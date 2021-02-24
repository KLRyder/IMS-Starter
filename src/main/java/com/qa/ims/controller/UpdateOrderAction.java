package com.qa.ims.controller;

import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum UpdateOrderAction {
    ADD("To add a new item to this order"),
    REMOVE("To remove an item from this order"),
    QUANTITY("To update the quantity of an item in this order"),
    CUSTOMER("To change the customer associated with this order"),
    CANCEL("To abort changing the order, discarding changes so far"),
    DONE("To finish editing and submit this order");

    public static final Logger LOGGER = LogManager.getLogger();

    private String description;

    UpdateOrderAction(String description) {
        this.description = description;
    }


    /**
     * Describes the action
     */
    public String getDescription() {
        return this.name() + ": " + this.description;
    }

    /**
     * Prints out all possible actions
     */
    public static void printActions() {
        for (UpdateOrderAction action : UpdateOrderAction.values()) {
            LOGGER.info(action.getDescription());
        }
    }

    /**
     * Gets an action based on a users input. If user enters a non-specified
     * enumeration, it will ask for another input.
     *
     * @return Action type
     */
    public static UpdateOrderAction getAction(Utils utils) {
        UpdateOrderAction action = null;
        do {
            try {
                action = UpdateOrderAction.valueOf(utils.getString().toUpperCase());
            } catch (IllegalArgumentException e) {
                LOGGER.error("Invalid selection please try again");
            }
        } while (action == null);
        return action;
    }
}
