package com.qa.ims.persistence.dao;

import com.qa.ims.exceptions.ItemNotFoundException;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ItemDAOTest {
    private final ItemDAO DAO = new ItemDAO();

    @Before
    public void setup() {
        DBUtils.connect();
        DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
    }

    @Test
    public void testCreate() {
        final Item created = new Item(3L, "test_create", 402.22);
        assertEquals(created, DAO.create(created));
    }

    @Test
    public void testReadAll() {
        List<Item> expected = new ArrayList<>();
        expected.add(new Item(1L, "test_item", 22.22));
        expected.add(new Item(2L, "item", 22.26));
        assertEquals(expected, DAO.readAll());
    }

    @Test
    public void testReadLatest() {
        assertEquals(new Item(2L, "item", 22.26), DAO.readLatest());
    }

    @Test
    public void testReadLatestNoItems() {
        DAO.delete(2);
        DAO.delete(1);
        assertThrows(ItemNotFoundException.class, DAO::readLatest);
    }

    @Test
    public void testRead() {
        final long ID = 1L;
        assertEquals(new Item(ID, "test_item", 22.22), DAO.read(ID));
    }

    @Test
    public void testReadNotExists() {
        final long ID = 1111111L;
        assertThrows(ItemNotFoundException.class, () -> DAO.read(ID));
    }

    @Test
    public void testUpdate() {
        final Item updated = new Item(1L, "test_update", 9999.9);
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
        assertEquals(new ArrayList<Item>(), DAO.readAll());
    }

    @Test
    public void readLatestBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.readLatest());
    }

    @Test
    public void createBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.create(new Item("test",2222)));
    }

    @Test
    public void readBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.read(1L));
    }

    @Test
    public void updateBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertNull(DAO.update(new Item(1L, "dasdas",222)));
    }

    @Test
    public void deleteBrokenDBTest(){
        DBUtils.getInstance().init("src/test/resources/sql-schema-broken.sql");
        assertEquals(0,DAO.delete(1));
    }
}
