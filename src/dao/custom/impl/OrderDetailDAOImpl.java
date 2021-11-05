package dao.custom.impl;

import dao.StatementUtil;
import dao.custom.OrderDetailDAO;

import entity.OrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("INSERT INTO `Order Detail` (orderId,itemCode, orderedQty,unitPrice,`discount(%)`) VALUES (?,?,?,?,?)",
                orderDetail.getOrderId(),orderDetail.getItemCode(),orderDetail.getOrderedQty(),orderDetail.getUnitPrice(),orderDetail.getDiscount());
    }

    @Override
    public boolean remove(String id) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("DELETE FROM `Order Detail` WHERE orderId=?",id);
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public OrderDetail search(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<OrderDetail> getAllById(String id) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orderDetails=new ArrayList<>();
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM `Order Detail` WHERE orderId=?",id);
        while (resultSet.next()) {
            orderDetails.add(new OrderDetail(
                    resultSet.getString("orderId"),
                    resultSet.getString("itemCode"),
                    resultSet.getInt("orderedQty"),
                    resultSet.getBigDecimal("unitPrice"),
                    resultSet.getBigDecimal("discount(%)")
            ));
        }
        return orderDetails;
    }
}
