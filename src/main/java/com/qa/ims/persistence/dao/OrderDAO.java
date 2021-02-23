package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO implements Dao<Order> {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Order> readAll() {
        return null;
    }

    @Override
    public Order read(Long id) {
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
        return null;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }

    public Order readLatest() {
        return null;
    }
}
