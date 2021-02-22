package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    @Test
    public void testEqualsAndHash() {
        EqualsVerifier.simple().forClass(Order.class).verify();
    }

    @Test
    public void testCompare(){
        Item i1 = new Item(1L, "itm", 11.11);
        Item i2 = new Item(2L, "item", 22.22);
        Item i3 = new Item(3L, "test_item", 33.33);

        ArrayList<Item>items = new ArrayList<>();
        items.add(i1);

        Order o1 = new Order(1L,new Customer("bob", "bobson"),items);
        items.add(i2);
        Order o2 = new Order(2L,new Customer("bobie", "bobsonson"),items);
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
    public void testTotalPrice(){
        Item i1 = new Item(1L, "itm", 11.11);
        Item i2 = new Item(2L, "item", 22.22);
        Item i3 = new Item(3L, "test_item", 33.33);

        ArrayList<Item>items = new ArrayList<>();
        items.add(i1);
        items.add(i2);
        items.add(i3);
        Order testOrder = new Order(new Customer("bob","bobson"), items);

        assertEquals(66.66,testOrder.getTotalCost());
    }
}
