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

    }

    @Test
    public void testReadAll() {
    }

    @Test
    public void testReadLatest() {

    }

    @Test
    public void testRead() {
    }

    @Test
    public void testUpdate() {
        Item[] items = {new Item(2L,"item",22.26),new Item(1L,"test_item", 22.22),new Item("test_item", 22.22)};
        final Order Updated = new Order(1L, new Customer("bob", "bobson"), new ArrayList<>(Arrays.asList(items)));
    }

    @Test
    public void testDelete() {
        assertEquals(1, DAO.delete(1));
    }
}
