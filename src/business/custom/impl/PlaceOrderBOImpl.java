package business.custom.impl;

import business.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.DBConnection;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {

        Connection connection = null;

        connection = DBConnection.getDbConnection().getConnection();
        boolean exist = orderDAO.ifOrderExist(orderDTO.getOrderId());

        if (exist) {
            return false;
        }

        connection.setAutoCommit(false);
        Order order = new Order(orderDTO.getOrderId(),orderDTO.getCustomerId(),orderDTO.getOrderDate(),orderDTO.getGrossAmount(),orderDTO.getNetTotal());
        boolean add = orderDAO.add(order);
        if (!add) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        for (OrderDetailDTO detail : orderDTO.getOrderDetail()) {
            OrderDetail orderDetail = new OrderDetail(detail.getOrderId(),detail.getItemCode(),detail.getOrderedQty(),detail.getUnitPrice(),detail.getDiscount());
            boolean addDetail = orderDetailDAO.add(orderDetail);
            if (!addDetail) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            Item temp = itemDAO.search(detail.getItemCode());
            int newQty=temp.getQtyOnHand()-detail.getOrderedQty();
            boolean update = itemDAO.updateQty(temp.getItemCode(),newQty);
            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId();
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItems=new ArrayList<>();
        ArrayList<Item> all = itemDAO.getAll();
        for (Item item : all) {
            allItems.add(new ItemDTO(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getQtyOnHand(),item.getUnitPrice()));
        }
        return allItems;
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(code);
        return new ItemDTO(search.getItemCode(),search.getDescription(),search.getPackSize(),search.getQtyOnHand(),search.getUnitPrice());
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.ifCustomerExist(id);
    }

    @Override
    public CustomerDTO searchCustomer(String s) throws SQLException, ClassNotFoundException {
        Customer search = customerDAO.search(s);
        return new CustomerDTO(search.getCustomerId(),search.getCustomerName(),search.getCustomerTitle(),search.getCustomerAddress(),search.getCity(),search.getProvince(),search.getPostalCode());
    }
}
