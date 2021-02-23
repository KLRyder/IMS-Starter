package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderDAOTest {
    private final OrderDAO DAO = new OrderDAO();

    @BeforeEach
    public void setup() {
        DBUtils.connect();
        DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
    }

    @Test
    public void testCreate() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> itemList = new HashMap<>();
        itemList.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        final Order order = new Order(2L, customer, itemList);
        assertEquals(order, DAO.create(order));
    }

    @Test
    public void testReadAll() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> itemList = new HashMap<>();
        itemList.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(ID, customer, itemList));
        assertEquals(orders, DAO.readAll());
    }

    @Test
    public void testReadLatest() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> itemList = new HashMap<>();
        itemList.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, itemList), DAO.readLatest());
    }

    @Test
    public void testRead() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> itemList = new HashMap<>();
        itemList.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, itemList), DAO.read(ID));
    }

    @Test
    public void testUpdate() {
        Item item1 = new Item(2L, "item", 22.26);
        Item item2 = new Item(1L, "test_item", 22.22);
        final HashMap<Item, Integer> itemList = new HashMap<>();
        itemList.put(item1, 1);
        itemList.put(item2, 2);
        final Order updated = new Order(1L, new Customer(2L, "bob", "bobson"), itemList);
        assertEquals(updated, DAO.update(updated));
    }

    @Test
    public void testDelete() {
        assertEquals(1, DAO.delete(1));
    }
}
