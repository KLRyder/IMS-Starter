package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OrderTest {

    public Item i1;
    public Item i2;
    public Item i3;
    public Customer customer;
    public HashMap<Item, Integer> items;

    @Before
    public void refreshTestVariables() {
        i1 = new Item(1L, "itm", 11.11);
        i2 = new Item(2L, "item", 22.22);
        i3 = new Item(3L, "test_item", 33.33);

        items = new HashMap<>();
        items.put(i1,1);
        items.put(i2,1);
        items.put(i3,1);

        customer = new Customer("bob", "bobson");
    }

    @Test
    public void testEqualsAndHash() {
        EqualsVerifier.simple().forClass(Order.class).verify();
    }

    @Test
    public void totalPriceTest() {
        Order testOrder = new Order(customer, items);

        assertEquals(66.66, testOrder.getTotalCost(), 0.001);
    }

    @Test
    public void removeItemTest() {
        Order testOrder = new Order(customer, items);

        assertTrue(testOrder.removeItem(new Item(1L, "itm", 11.11)));

        items = new HashMap<>();
        items.put(i2,1);
        items.put(i3,1);
        Order expectedOrder = new Order(customer, items);

        assertEquals(expectedOrder, testOrder);

        assertFalse(testOrder.removeItem(i1));
    }

    @Test
    public void addItemTest() {
        Order expectedOrder = new Order(customer, items);

        Order testOrder = new Order(customer, new HashMap<>());
        testOrder.addItem(i1);
        testOrder.addItem(i2);
        testOrder.addItem(i3);

        assertEquals(expectedOrder, testOrder);
    }
}
