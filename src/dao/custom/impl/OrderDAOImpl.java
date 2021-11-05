package dao.custom.impl;

import dao.StatementUtil;
import dao.custom.OrderDAO;
import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean add(Order order) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("INSERT INTO `Order` (orderId,customerId,orderDate,grossAmount,netTotal) VALUES (?,?,?,?,?)",
                order.getOrderId(),order.getCustomerId(),order.getOrderDate(),order.getGrossAmount(),order.getNetTotal());
    }

    @Override
    public boolean remove(String id) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("DELETE FROM `Order` WHERE orderId=?",id);
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("UPDATE `Order` SET customerId=?,orderDate=?,grossAmount=?,netTotal=? WHERE orderId=?",
                order.getCustomerId(),order.getOrderDate(),order.getGrossAmount(),order.getNetTotal(),order.getOrderId());
    }

    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Order> allOrders=new ArrayList<>();
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM `Order`");
        while (resultSet.next()) {
            allOrders.add(new Order(
                    resultSet.getString("orderId"),
                    resultSet.getString("customerId"),
                    LocalDate.parse(resultSet.getString("orderDate")),
                    resultSet.getBigDecimal("grossAmount"),
                    resultSet.getBigDecimal("netTotal")
            ));
        }
        return allOrders;
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst =StatementUtil.executeQuery("SELECT * FROM `Order` WHERE orderId=?",id);
        rst.next();
        return new Order(rst.getString("orderId"),rst.getString("customerId"),LocalDate.parse(rst.getString("orderDate")),rst.getBigDecimal("grossAmount"),rst.getBigDecimal("netTotal"));
    }

    @Override
    public boolean ifOrderExist(String id) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeQuery("SELECT orderId FROM `Order` WHERE orderId=?",id).next();
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = StatementUtil.executeQuery("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1;");
        return rst.next() ? String.format("OD%03d", (Integer.parseInt(rst.getString("orderId").replace("OD", "")) + 1)) : "OD001";
    }
}
