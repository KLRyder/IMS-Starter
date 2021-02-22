package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        final ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        ArrayList<Order> orders = new ArrayList<>();
        final Order order = new Order(2L, customer, itemList);
        assertEquals(order, DAO.create(order));
    }

    @Test
    public void testReadAll() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(ID, customer, itemList));
        assertEquals(orders, DAO.readAll());
    }

    @Test
    public void testReadLatest() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, itemList), DAO.readLatest());
    }

    @Test
    public void testRead() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, itemList), DAO.read(ID));
    }

    @Test
    public void testUpdate() {
        Item[] items = {new Item(2L, "item", 22.26), new Item(1L, "test_item", 22.22),
                new Item(1L, "test_item", 22.22)};
        final Order updated = new Order(1L, new Customer(2L, "bob", "bobson"),
                new ArrayList<>(Arrays.asList(items)));
        assertEquals(updated, DAO.update(updated));
    }

    @Test
    public void testDelete() {
        assertEquals(1, DAO.delete(1));
    }
}
