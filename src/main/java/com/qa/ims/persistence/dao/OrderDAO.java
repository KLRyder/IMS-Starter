package com.qa.ims.persistence.dao;

import com.qa.ims.exceptions.OrderNotFoundException;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrderDAO implements Dao<Order> {
    public static final Logger LOGGER = LogManager.getLogger();
    private final ItemDAO itemDAO = new ItemDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    public List<Order> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `order` ORDER BY idorder")) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(modelFromResultSet(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Order read(Long id) throws OrderNotFoundException {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM `order` WHERE idorder = ?")) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    throw new OrderNotFoundException();
                }
                return modelFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Order create(Order order) {
        try {
            Connection connection = DBUtils.getInstance().getConnection();

            connection.setAutoCommit(false);

            // get index of the next order to insert. needed for orderlink creation se we cant rely on the
            // tables auto increment
            ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(idorder) FROM `order`");
            rs.next();
            int index = rs.getInt("MAX(idorder)") + 1;

            //create order in order table
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `order`(idorder, custid) VALUES (?,?)");
            statement.setLong(1, index);
            statement.setLong(2, order.getCustomer().getId());

            statement.execute();

            //create orderLink in order_link table for each attached item(if any)
            for (Item item : order.getItems().keySet()) {
                statement = connection.prepareStatement(
                        "INSERT INTO `order_link`(orderid,itemid,quantity) VALUES (?,?,?)");
                statement.setLong(1, index);
                statement.setLong(2, item.getId());
                statement.setInt(3, order.getItems().get(item));
                statement.execute();
            }

            connection.commit();
            connection.close();
            return readLatest();

        } catch (SQLException throwables) {
            LOGGER.debug(throwables);
            LOGGER.error(throwables.getMessage());
        }
        return null;
    }

    @Override
    public Order update(Order order) {
        try {
            try {
                Order currentOrder = read(order.getId());

                if (currentOrder == null) {
                    throw new SQLException();
                }

                Connection connection = DBUtils.getInstance().getConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement;

                // Customer updated -> update in order table;
                if (!currentOrder.getCustomer().equals(order.getCustomer())) {
                    statement = connection.prepareStatement(
                            "UPDATE `order` SET custid = ? WHERE idorder = ?");
                    statement.setLong(1, order.getCustomer().getId());
                    statement.setLong(2, order.getId());
                    statement.executeUpdate();
                }

                // Check for change in orders item
                if (!currentOrder.getItems().equals(order.getItems())) {
                    Set<Item> currentItems = currentOrder.getItems().keySet();
                    Set<Item> updatedItems = order.getItems().keySet();


                    Set<Item> newItems = new HashSet<>(updatedItems);
                    newItems.removeAll(currentItems);

                    Set<Item> itemsToRemove = new HashSet<>(currentItems);
                    itemsToRemove.removeAll(updatedItems);

                    for (Item newItem : newItems) {
                        statement = connection.prepareStatement(
                                "INSERT INTO `order_link`(orderid, itemid, quantity) VALUES (?,?,?)");
                        statement.setLong(1, order.getId());
                        statement.setLong(2, newItem.getId());
                        statement.setInt(3, order.getItems().get(newItem));
                        statement.execute();
                    }


                    //Check for extra item in current items -> remove extra item from order_link table
                    for (Item extraItem : itemsToRemove) {
                        statement = connection.prepareStatement("DELETE FROM `order_link` WHERE orderid = ? and itemid = ?");
                        statement.setLong(1, order.getId());
                        statement.setLong(2, extraItem.getId());
                        statement.execute();
                    }

                    //Check for item quantity change -> update quantity in order_link
                    for (Item item : updatedItems) {
                        int oldVal = currentOrder.getItems().getOrDefault(item, 0);
                        int newVal = order.getItems().get(item);
                        if (oldVal != newVal) {
                            statement = connection.prepareStatement(
                                    "UPDATE `order_link` SET quantity = ? WHERE orderid = ? AND itemid = ?");
                            statement.setInt(1, newVal);
                            statement.setLong(2, order.getId());
                            statement.setLong(3, item.getId());
                            statement.execute();
                        }
                    }
                }

                connection.commit();
                connection.close();

                return read(order.getId());
            } catch (OrderNotFoundException e) {
                LOGGER.info("Order with id " + order.getId() + " cant be found in table");
                return null;
            }
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public int delete(long id) {
        try (Connection connection = DBUtils.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `order` WHERE idorder = ?");
            statement.setLong(1, id);
            int toReturn = statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM order_link WHERE orderid = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() + toReturn;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Order modelFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("idorder");
        Long custId = rs.getLong("custid");

        return new Order(id, customerDAO.read(custId), readItemsFromOrder(id));
    }

    public Map<Item, Integer> readItemsFromOrder(Long orderId) {
        Map<Item, Integer> items = new HashMap<>();
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM `order_link` WHERE orderid = " + orderId)) {
            while (rs.next()) {
                items.put(itemDAO.read(rs.getLong("itemid")), rs.getInt("quantity"));
            }
            return items;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public Order readLatest() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `order` ORDER BY idorder DESC LIMIT 1")) {
            if (resultSet.next()) {
                return modelFromResultSet(resultSet);
            } else throw new OrderNotFoundException();
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
