package com.qa.ims.controllers;

import com.qa.ims.controller.OrderController;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    public Customer testCust;
    public Item testItem1;
    public Item testItem2;
    public ArrayList<Item> testItems;

    @Mock
    private Utils utils;

    @Mock
    private OrderDAO dao;

    @InjectMocks
    private OrderController controller;

    @BeforeEach
    public void refreshTestObjects(){
        testCust = new Customer("test","cust");
        testItem1 = new Item("test_item1",22.22);
        testItem2 = new Item("test_item2",31.86);
        testItems = new ArrayList<>();
        testItems.add(testItem1);
        testItems.add(testItem1);
        testItems.add(testItem2);
    }

    @Test
    public void testCreate() {
        Order testOrder = new Order(testCust,testItems);


    }

    @Test
    public void testReadAll() {

    }

    @Test
    public void testUpdate() {

    }

    @Test
    public void testDelete() {
        final long ID = 1L;

        Mockito.when(utils.getLong()).thenReturn(ID);
        Mockito.when(dao.delete(ID)).thenReturn(1);

        assertEquals(1L, this.controller.delete());

        Mockito.verify(utils, Mockito.times(1)).getLong();
        Mockito.verify(dao, Mockito.times(1)).delete(ID);

    }
}
