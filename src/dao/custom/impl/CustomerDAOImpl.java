package dao.custom.impl;

import dao.StatementUtil;
import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Customer customer) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate(
                "INSERT INTO Customer (customerId,customerName,customerTitle,customerAddress,city,province,postalCode) VALUES (?,?,?,?,?,?,?)",
                customer.getCustomerId(),customer.getCustomerName(),customer.getCustomerTitle(),customer.getCustomerAddress(),
                customer.getCity(),customer.getProvince(),customer.getPostalCode());
    }

    @Override
    public boolean remove(String id) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("DELETE FROM Customer WHERE customerId=?",id);
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("UPDATE Customer SET customerName=?,customerTitle=?,customerAddress=?,city=?,province=?,postalCode=? WHERE customerId=?",
                customer.getCustomerName(),customer.getCustomerTitle(),customer.getCustomerAddress(),
                customer.getCity(),customer.getProvince(),customer.getPostalCode(),customer.getCustomerId());
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> allCustomers=new ArrayList<>();
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM Customer");
        while (resultSet.next()) {
            allCustomers.add(new Customer(
                    resultSet.getString("customerId"),
                    resultSet.getString("customerName"),
                    resultSet.getString("customerTitle"),
                    resultSet.getString("customerAddress"),
                    resultSet.getString("city"),
                    resultSet.getString("province"),
                    resultSet.getString("postalCode")
            ));
        }
        return allCustomers;
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM Customer WHERE customerId=?", id);
        resultSet.next();
        return new Customer(
                resultSet.getString("customerId"),
                resultSet.getString("customerName"),
                resultSet.getString("customerTitle"),
                resultSet.getString("customerAddress"),
                resultSet.getString("city"),
                resultSet.getString("province"),
                resultSet.getString("postalCode")
        );
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeQuery("SELECT customerId FROM Customer WHERE customerId=?",id).next();
    }
}
