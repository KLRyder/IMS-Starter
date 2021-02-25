package com.qa.ims.persistence.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class ItemTest {
    @Test
    public void testEqualsAndHash() {
        EqualsVerifier.simple().forClass(Item.class).verify();
    }

    @Test
    public void testCompare(){
        Item c1 = new Item(1L, "itm", 11.11);
        Item c2 = new Item(2L, "item", 22.22);
        Item c3 = new Item(3L, "test_item", 33.33);

        ArrayList<Item> itemsUnsorted = new ArrayList<>();
        itemsUnsorted.add(c3);
        itemsUnsorted.add(c1);
        itemsUnsorted.add(c2);

        ArrayList<Item> items = new ArrayList<>();
        items.add(c1);
        items.add(c2);
        items.add(c3);

        Collections.sort(itemsUnsorted);

        assertEquals(itemsUnsorted, items);
    }
}
