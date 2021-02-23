package com.qa.ims.controller;

import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ItemController implements CrudController<Item> {


    public static final Logger LOGGER = LogManager.getLogger();

    private ItemDAO itemDAO;
    private Utils utils;

    public ItemController(ItemDAO itemDAO, Utils utils) {
        super();
        this.itemDAO = itemDAO;
        this.utils = utils;
    }

    @Override
    public List<Item> readAll() {
        List<Item> items = itemDAO.readAll();
        for (Item i :
                items) {
            LOGGER.info(i);
        }
        return items;
    }

    @Override
    public Item create() {
        LOGGER.info("Please enter an item name");
        String name = utils.getString();
        LOGGER.info("Please enter the price for " + name);
        double price = utils.getDouble();
        Item item = itemDAO.create(new Item(name, price));
        LOGGER.info("item created");
        return item;
    }

    //TODO change single values rather than whole item. Potentially read item and ask what to update?
    @Override
    public Item update() {
        LOGGER.info("Please enter the id of the item you would like to update");
        Long id = utils.getLong();
        LOGGER.info("Please enter a new name");
        String name = utils.getString();
        LOGGER.info("Please enter a new price");
        double price = utils.getDouble();
        Item item = itemDAO.update(new Item(id, name, price));
        LOGGER.info("Item Updated");
        return item;
    }

    //TODO make the user confirm that they wish to delete the item. Potentaly read item to confirm first?
    @Override
    public int delete() {
        LOGGER.info("Please enter tje id  of the item you want to delete");
        Long id = utils.getLong();
        return itemDAO.delete(id);
    }
}
