package com.qa.ims.controllers;

import com.qa.ims.controller.OrderController;
import com.qa.ims.exceptions.CustomerNotFoundException;
import com.qa.ims.exceptions.ItemNotFoundException;
import com.qa.ims.exceptions.OrderNotFoundException;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    public Customer testCust;
    public Item testItem1;
    public Item testItem2;
    public HashMap<Item,Integer> testItems;
    public Order testOrder;

    @Mock
    private Utils utils;

    @Mock
    private OrderDAO orderDao;

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private OrderController controller;

    @BeforeEach
    public void refreshTestObjects() {
        testCust = new Customer(1L, "test", "cust");
        testItem1 = new Item(1L, "test_item1", 22.22);
        testItem2 = new Item(2L, "test_item2", 31.86);
        testItems = new HashMap<>();
        testItems.put(testItem1,2);
        testItems.put(testItem2,1);
    }

    @Test
    public void testCreate() {
        testOrder = new Order(testCust, new HashMap<>());

        when(utils.getLong()).thenReturn(1L);
        when(orderDao.create(testOrder)).thenReturn(testOrder);
        when(customerDAO.read(1L)).thenReturn(testCust);

        assertEquals(testOrder, controller.create());

        verify(utils, times(1)).getLong();
        verify(orderDao, times(1)).create(testOrder);
        verify(customerDAO, times(1)).read(1L);
    }

    @Test
    public void testCreateInvalidCustomer() {
        testOrder = new Order(new Customer(11111L, "fakie", "mcfakeface"), new HashMap<>());
        when(utils.getLong()).thenReturn(11111L);
        when(customerDAO.read(11111L)).thenThrow(new CustomerNotFoundException());

        assertNull(controller.create());

        verify(utils, times(1)).getLong();
        verify(customerDAO, times(1)).read(11111L);
    }

    @Test
    public void testReadAll() {
        List<Order> orders = new ArrayList<>();
        testOrder = new Order(testCust, testItems);
        orders.add(testOrder);

        when(orderDao.readAll()).thenReturn(orders);

        assertEquals(orders, controller.readAll());

        verify(orderDao, times(1)).readAll();
    }

    @Test
    public void updateOrderNotExistsTest() {
        when(this.utils.getLong()).thenReturn(1L);
        when(orderDao.read(1L)).thenThrow(new OrderNotFoundException());

        assertNull(controller.update());

        verify(utils, times(1)).getLong();
    }

    @Test
    public void updateCustomerTest() {
        testOrder = new Order(1L, testCust, testItems);
        Customer newCust = new Customer(2L, "J.Jonah", "Jameson");
        Order updatedOrder = new Order(1L, newCust, testItems);

        when(this.utils.getLong()).thenReturn(1L, 2L);
        when(utils.getString()).thenReturn("customer","done");
        when(orderDao.read(1L)).thenReturn(testOrder);
        when(customerDAO.read(2L)).thenReturn(newCust);
        when(orderDao.update(updatedOrder)).thenReturn(updatedOrder);

        assertEquals(updatedOrder, controller.update());

        verify(utils, times(2)).getLong();
        verify(utils, times(2)).getString();
        verify(orderDao, times(1)).read(1L);
        verify(orderDao, times(1)).update(updatedOrder);
        verify(customerDAO, times(1)).read(2L);
    }

    @Test
    public void updateCustomerNotExistsTest() {
        testOrder = new Order(1L, testCust, testItems);
        when(this.utils.getLong()).thenReturn(1L, 2L);
        when(utils.getString()).thenReturn("customer");
        when(orderDao.read(1L)).thenReturn(testOrder);
        when(customerDAO.read(2L)).thenThrow(new CustomerNotFoundException());

        assertEquals(testOrder, controller.update());

        verify(this.utils, times(2)).getLong();
        verify(utils, times(1)).getString();
        verify(orderDao, times(1)).read(1L);
        verify(customerDAO, times(1)).read(2L);
    }

    @Test
    public void updateAddItemTest() {
        testOrder = new Order(1L, testCust, testItems);
        Order updatedOrder = new Order(1L, testCust, (Map<Item, Integer>) testItems.clone());
        updatedOrder.addItem(testItem1, 2);

        when(utils.getLong()).thenReturn(1L, 1L);
        when(utils.getString()).thenReturn("add");
        when(utils.getInt()).thenReturn(2);

        when(orderDao.read(1L)).thenReturn(testOrder);
        when(orderDao.update(updatedOrder)).thenReturn(updatedOrder);
        when(itemDAO.read(1L)).thenReturn(testItem1);

        assertEquals(updatedOrder, controller.update());

        verify(utils, times(2)).getLong();
        verify(utils, times(1)).getString();
        verify(utils, times(1)).getInt();
        verify(orderDao, times(1)).read(1L);
        verify(orderDao, times(1)).update(updatedOrder);
        verify(itemDAO, times(1)).read(1L);
    }

    @Test
    public void updateAddItemNotExistsTest() {
        testOrder = new Order(1L, testCust, testItems);

        when(utils.getLong()).thenReturn(1L, 2L);
        when(utils.getString()).thenReturn("add");

        when(orderDao.read(1L)).thenReturn(testOrder);
        when(itemDAO.read(2L)).thenThrow(new ItemNotFoundException());

        assertEquals(testOrder, controller.update());

        verify(utils, times(1)).getLong();
        verify(utils, times(1)).getString();
        verify(orderDao, times(1)).read(1L);
        verify(itemDAO, times(1)).read(2L);
    }

    @Test
    public void updateRemoveItemTest() {
        testOrder = new Order(1L, testCust, testItems);

        
    }

    @Test
    public void updateRemoveItemNotInOrderTest() {

    }

    @Test
    public void testDelete() {
        final long ID = 1L;

        when(utils.getLong()).thenReturn(ID);
        when(orderDao.delete(ID)).thenReturn(1);

        assertEquals(1L, this.controller.delete());

        verify(utils, times(1)).getLong();
        verify(orderDao, times(1)).delete(ID);

    }
}
