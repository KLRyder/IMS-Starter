package com.qa.ims.controllers;

import com.qa.ims.controller.OrderController;
import com.qa.ims.exceptions.CustomerNotFoundException;
import com.qa.ims.exceptions.ItemNotFoundException;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        Mockito.when(utils.getLong()).thenReturn(1L);
        Mockito.when(orderDao.create(testOrder)).thenReturn(testOrder);

        assertEquals(testOrder, controller.create());

        Mockito.verify(utils, Mockito.times(1)).getLong();
    }

    @Test
    public void testCreateInvalidCustomer() {
        testOrder = new Order(new Customer(11111L, "fakie", "mcfakeface"), new HashMap<>());
        Mockito.when(utils.getLong()).thenReturn(11111L);
        Mockito.when(orderDao.create(testOrder)).thenThrow(new CustomerNotFoundException());

        assertNull(controller.create());

        Mockito.verify(utils, Mockito.times(1)).getLong();
        Mockito.verify(orderDao, Mockito.times(1)).create(testOrder);
    }

    @Test
    public void testReadAll() {
        List<Order> orders = new ArrayList<>();
        testOrder = new Order(testCust, testItems);
        orders.add(testOrder);

        Mockito.when(orderDao.readAll()).thenReturn(orders);

        assertEquals(orders, controller.readAll());

        Mockito.verify(orderDao, Mockito.times(1)).readAll();
    }

    @Test
    public void testUpdateAddItem() {
        testOrder = new Order(1L, testCust, testItems);
        Order expectedOrder = new Order(1L, testCust, (Map<Item, Integer>) testItems.clone());
        expectedOrder.addItem(testItem1 , 2);

        Mockito.when(this.utils.getLong()).thenReturn(1L, 1L);
        Mockito.when(utils.getString()).thenReturn("add");
        Mockito.when(utils.getInt()).thenReturn(2);

        Mockito.when(orderDao.read(1L)).thenReturn(testOrder);
        Mockito.when(orderDao.update(expectedOrder)).thenReturn(expectedOrder);
        Mockito.when(itemDAO.read(1L)).thenReturn(testItem1);

        assertEquals(expectedOrder, controller.update());

        Mockito.verify(this.utils, Mockito.times(2)).getLong();
        Mockito.verify(this.utils, Mockito.times(1)).getString();
        Mockito.verify(this.utils, Mockito.times(1)).getInt();
        Mockito.verify(this.orderDao, Mockito.times(1)).update(expectedOrder);

    }

    @Test
    public void updateOrderNotExistsTest() {
        Mockito.when(this.utils.getLong()).thenReturn(1L);
        Mockito.when(orderDao.read(1L)).thenThrow(new CustomerNotFoundException());

        assertNull(controller.update());

        Mockito.verify(utils, Mockito.times(1)).getLong();
    }

    @Test
    public void updateCustomerTest() {
        testOrder = new Order(1L, testCust, testItems);
        Customer newCust = new Customer(2L, "J.Jonah", "Jameson");
        Order updatedOrder = new Order(1L, newCust, testItems);

        Mockito.when(this.utils.getLong()).thenReturn(1L, 2L);
        Mockito.when(utils.getString()).thenReturn("customer");
        Mockito.when(orderDao.read(1L)).thenReturn(testOrder);
        Mockito.when(customerDAO.read(2L)).thenReturn(newCust);
        Mockito.when(orderDao.update(updatedOrder)).thenReturn(updatedOrder);

        assertEquals(updatedOrder, controller.update());

        Mockito.verify(utils, Mockito.times(2)).getLong();
        Mockito.verify(utils, Mockito.times(1)).getString();
        Mockito.verify(orderDao, Mockito.times(1)).read(1L);
        Mockito.verify(orderDao, Mockito.times(1)).update(updatedOrder);
        Mockito.verify(customerDAO, Mockito.times(1)).read(1L);
    }

    @Test
    public void updateCustomerNotExistsTest() {
        testOrder = new Order(1L, testCust, testItems);
        Mockito.when(this.utils.getLong()).thenReturn(1L, 2L);
        Mockito.when(utils.getString()).thenReturn("customer");
        Mockito.when(orderDao.read(1L)).thenReturn(testOrder);
        Mockito.when(customerDAO.read(2L)).thenThrow(new CustomerNotFoundException());

        assertEquals(testOrder, controller.update());

        Mockito.verify(this.utils, Mockito.times(2)).getLong();
        Mockito.verify(utils, Mockito.times(1)).getString();
        Mockito.verify(orderDao, Mockito.times(1)).read(1L);
        Mockito.verify(customerDAO, Mockito.times(1)).read(2L);
    }

    @Test
    public void updateAddItemTest() {
        testOrder = new Order(1L, testCust, testItems);
        Order updatedOrder = new Order(1L, testCust, (Map<Item, Integer>) testItems.clone());
        updatedOrder.addItem(testItem1, 2);

        Mockito.when(utils.getLong()).thenReturn(1L, 1L);
        Mockito.when(utils.getString()).thenReturn("add");
        Mockito.when(utils.getInt()).thenReturn(2);

        Mockito.when(orderDao.read(1L)).thenReturn(testOrder);
        Mockito.when(orderDao.update(updatedOrder)).thenReturn(updatedOrder);
        Mockito.when(itemDAO.read(1L)).thenReturn(testItem1);

        assertEquals(updatedOrder, controller.update());

        Mockito.verify(utils, Mockito.times(2)).getLong();
        Mockito.verify(utils, Mockito.times(1)).getString();
        Mockito.verify(utils, Mockito.times(1)).getInt();
        Mockito.verify(orderDao, Mockito.times(1)).read(1L);
        Mockito.verify(orderDao, Mockito.times(1)).update(updatedOrder);
        Mockito.verify(itemDAO, Mockito.times(1)).read(1L);
    }

    @Test
    public void updateAddItemNotExistsTest() {
        testOrder = new Order(1L, testCust, testItems);

        Mockito.when(utils.getLong()).thenReturn(1L, 2L);
        Mockito.when(utils.getString()).thenReturn("add");

        Mockito.when(orderDao.read(1L)).thenReturn(testOrder);
        Mockito.when(itemDAO.read(2L)).thenThrow(new ItemNotFoundException());

        assertEquals(testOrder, controller.update());

        Mockito.verify(utils, Mockito.times(1)).getLong();
        Mockito.verify(utils, Mockito.times(1)).getString();
        Mockito.verify(orderDao, Mockito.times(1)).read(1L);
        Mockito.verify(itemDAO, Mockito.times(1)).read(2L);
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

        Mockito.when(utils.getLong()).thenReturn(ID);
        Mockito.when(orderDao.delete(ID)).thenReturn(1);

        assertEquals(1L, this.controller.delete());

        Mockito.verify(utils, Mockito.times(1)).getLong();
        Mockito.verify(orderDao, Mockito.times(1)).delete(ID);

    }
}
