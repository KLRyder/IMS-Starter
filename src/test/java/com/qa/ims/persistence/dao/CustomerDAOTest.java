package com.qa.ims.persistence.dao;


import java.util.ArrayList;
import java.util.List;


import com.qa.ims.exceptions.CustomerNotFoundException;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.utils.DBUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDAOTest {

    private final CustomerDAO DAO = new CustomerDAO();

    @BeforeEach
    public void setup() {
        DBUtils.connect();
        DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
    }

    @Test
    public void testCreate() {
        final Customer created = new Customer(3L, "chris", "perrins");
        assertEquals(created, DAO.create(created));
    }

    @Test
    public void testReadAll() {
        List<Customer> expected = new ArrayList<>();
        expected.add(new Customer(1L, "jordan", "harrison"));
        expected.add(new Customer(2L, "bob", "bobson"));
        assertEquals(expected, DAO.readAll());
    }

    @Test
    public void testReadLatest() {
        assertEquals(new Customer(2L, "bob", "bobson"), DAO.readLatest());
    }

    @Test
    public void testReadLatestNoItems() {
        DAO.delete(2);
        DAO.delete(1);
        assertThrows(CustomerNotFoundException.class, DAO::readLatest);
    }


    @Test
    public void testRead() {
        final long ID = 1L;
        assertEquals(new Customer(ID, "jordan", "harrison"), DAO.read(ID));
    }

    @Test
    public void testReadNotExists() {
        final long ID = 1111111L;
        assertThrows(CustomerNotFoundException.class, () -> DAO.read(ID));
    }

    @Test
    public void testUpdate() {
        final Customer updated = new Customer(1L, "chris", "perrins");
        assertEquals(updated, DAO.update(updated));

    }

    @Test
    public void testDelete() {
        assertEquals(1, DAO.delete(1));
    }


    //Tests for incorrectly setup database
    @Test
    public void readAllBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertEquals(new ArrayList<Customer>(), DAO.readAll());
    }

    @Test
    public void readLatestBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.readLatest());
    }

    @Test
    public void createBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.create(new Customer("test","test")));
    }

    @Test
    public void readBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.read(1L));
    }

    @Test
    public void updateBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.update(new Customer(1L, "dasdas","adsads")));
    }

    @Test
    public void deleteBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertEquals(0,DAO.delete(1));
    }
}
