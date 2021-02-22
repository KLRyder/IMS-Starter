package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    public Item i1;
    public Item i2;
    public Item i3;
    public Customer customer;
    public ArrayList<Item> items;

    @BeforeEach
    public void refreshTestVariables() {
        i1 = new Item(1L, "itm", 11.11);
        i2 = new Item(2L, "item", 22.22);
        i3 = new Item(3L, "test_item", 33.33);

        items = new ArrayList<>();
        items.add(i1);
        items.add(i2);
        items.add(i3);

        customer = new Customer("bob", "bobson");
    }

    @Test
    public void testEqualsAndHash() {
        EqualsVerifier.simple().forClass(Order.class).verify();
    }

    @Test
    public void testCompare() {

        ArrayList<Item> items = new ArrayList<>();
        items.add(i1);

        Order o1 = new Order(1L, customer, items);
        items.add(i2);
        Order o2 = new Order(2L, new Customer("bobie", "bobsonson"), items);
        items.add(i3);
        Order o3 = new Order(3L, new Customer("Robbert", "Robson"), items);
        ArrayList<Order> ordersUnsorted = new ArrayList<>();
        ordersUnsorted.add(o3);
        ordersUnsorted.add(o1);
        ordersUnsorted.add(o2);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        Collections.sort(ordersUnsorted);

        assertEquals(ordersUnsorted, orders);
    }

    @Test
    public void totalPriceTest() {
        Order testOrder = new Order(customer, items);

        assertEquals(66.66, testOrder.getTotalCost());
    }

    @Test
    public void removeItemTest() {
        Order testOrder = new Order(customer, items);

        assertTrue(testOrder.removeItem(new Item(1L, "itm", 11.11)));

        items = new ArrayList<>();
        items.add(i2);
        items.add(i3);
        Order expectedOrder = new Order(customer, items);

        assertEquals(expectedOrder, testOrder);

        assertFalse(testOrder.removeItem(i1));
    }

    @Test
    public void addItemTest() {
        Order expectedOrder = new Order(customer, items);

        Order testOrder = new Order(customer, new ArrayList<>());
        testOrder.addItem(i1);
        testOrder.addItem(i2);
        testOrder.addItem(i3);

        assertEquals(expectedOrder, testOrder);
    }
}
