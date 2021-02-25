package com.qa.ims.persistence.dao;

import com.qa.ims.exceptions.OrderNotFoundException;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;


public class OrderDAOTest {
    private final OrderDAO DAO = new OrderDAO();

    @Before
    public void setup() {
        DBUtils.connect();
        DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
    }

    @Test
    public void testCreate() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        final Order order = new Order(2L, customer, items);
        assertEquals(order, DAO.create(order));
    }

    @Test
    public void testReadAll() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(ID, customer, items));
        assertEquals(orders, DAO.readAll());
    }

    @Test
    public void testReadLatest() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, items), DAO.readLatest());
    }

    @Test
    public void testReadLatestNotFound() {
        DAO.delete(1);
        assertThrows(OrderNotFoundException.class, DAO::readLatest);
    }

    @Test
    public void testRead() {
        final long ID = 1L;
        final Item item = new Item(ID, "test_item", 22.22);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item, 1);
        final Customer customer = new Customer(1L, "jordan", "harrison");
        assertEquals(new Order(ID, customer, items), DAO.read(ID));
    }

    @Test
    public void testReadNotFound() {
        final long ID = 9999L;
        assertThrows(OrderNotFoundException.class, () -> DAO.read(ID));
    }

    @Test
    public void testUpdate() {
        Item item1 = new Item(2L, "item", 22.26);
        Item item2 = new Item(1L, "test_item", 22.22);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item1, 1);
        items.put(item2, 2);
        final Order updated = new Order(1L, new Customer(2L, "bob", "bobson"), items);
        assertEquals(updated, DAO.update(updated));
    }

    @Test
    public void testUpdateRemoveItem() {
        Item item1 = new Item(2L, "item", 22.26);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item1, 1);
        final Order updated = new Order(1L, new Customer(1L, "jordan", "harrison"), items);
        assertEquals(updated, DAO.update(updated));
    }

    @Test
    public void testUpdateNotFound() {
        Item item1 = new Item(2L, "item", 22.26);
        final HashMap<Item, Integer> items = new HashMap<>();
        items.put(item1, 1);
        final Order updated = new Order(42352345L, new Customer(1L, "jordan", "harrison"), items);
        assertNull(DAO.update(updated));
    }

    @Test
    public void testDelete() {
        assertEquals(1, DAO.delete(1));
    }



    //Tests for incorrectly setup database
    @Test
    public void readAllBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertEquals(new ArrayList<Order>(), DAO.readAll());
    }

    @Test
    public void readLatestBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.readLatest());
    }

    @Test
    public void createBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.create(new Order(1L, new Customer("",""),new HashMap<>())));
    }

    @Test
    public void readBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.read(1L));
    }

    @Test
    public void updateBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.update(new Order(1L, new Customer("",""),new HashMap<>())));
    }

    @Test
    public void deleteBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertEquals(0,DAO.delete(1));
    }
}
