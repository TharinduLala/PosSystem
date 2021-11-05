package dao.custom.impl;

import dao.StatementUtil;
import dao.custom.QueryDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT COUNT(itemCode) from Item");
        if (resultSet.next()) {
            return resultSet.getInt("COUNT(itemCode)");
        } else {
            return 0;
        }
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT COUNT(customerId) from Customer;");
        if (resultSet.next()) {
            return resultSet.getInt("COUNT(customerId)");
        } else {
            return 0;
        }
    }

    @Override
    public int getDailyOrdersCount(String date) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT COUNT(orderId) from `order` WHERE orderDate=?;",date);
        if (resultSet.next()) {
            return resultSet.getInt("COUNT(orderId)");
        } else {
            return 0;
        }
    }
}
