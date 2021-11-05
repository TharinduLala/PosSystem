package business.custom.impl;

import business.custom.ManageOrderBO;
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

public class ManageOrderBOImpl implements ManageOrderBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

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
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer search = customerDAO.search(id);
        return new CustomerDTO(search.getCustomerId(),search.getCustomerName(),search.getCustomerTitle(),search.getCustomerAddress(),search.getCity(),search.getProvince(),search.getPostalCode());
    }

    @Override
    public boolean removeOrder(String id) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        connection = DBConnection.getDbConnection().getConnection();
        boolean exist = ifOrderExist(id);

        if (!exist) {
            return false;
        }

        connection.setAutoCommit(false);
        ArrayList<OrderDetail> allById = orderDetailDAO.getAllById(id);
        for (OrderDetail orderDetail : allById) {
            Item temp = itemDAO.search(orderDetail.getItemCode());
            int tempQty=temp.getQtyOnHand()+orderDetail.getOrderedQty();
            boolean update = itemDAO.updateQty(temp.getItemCode(),tempQty);
            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        boolean remove = orderDAO.remove(id);
        if (!remove) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public boolean updateOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        connection = DBConnection.getDbConnection().getConnection();
        boolean exist = ifOrderExist(orderDTO.getOrderId());

        if (!exist) {
            return false;
        }

        connection.setAutoCommit(false);
        Order order = new Order(orderDTO.getOrderId(),orderDTO.getCustomerId(),orderDTO.getOrderDate(),orderDTO.getGrossAmount(),orderDTO.getNetTotal());
        boolean updateOrder = orderDAO.update(order);
        if (!updateOrder) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        ArrayList<OrderDetail> allById = orderDetailDAO.getAllById(orderDTO.getOrderId());
        for (OrderDetail orderDetail : allById) {
            Item temp = itemDAO.search(orderDetail.getItemCode());
            int tempQty=temp.getQtyOnHand()+orderDetail.getOrderedQty();
            boolean update = itemDAO.updateQty(temp.getItemCode(),tempQty);
            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        boolean clearDetails = orderDetailDAO.remove(orderDTO.getOrderId());
        if (!clearDetails) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }else {

            for (OrderDetailDTO detail : orderDTO.getOrderDetail()) {
                OrderDetail orderDetail = new OrderDetail(detail.getOrderId(), detail.getItemCode(), detail.getOrderedQty(), detail.getUnitPrice(), detail.getDiscount());
                boolean addDetail = orderDetailDAO.add(orderDetail);
                if (!addDetail) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }

                Item temp = itemDAO.search(detail.getItemCode());
                int newQty = temp.getQtyOnHand() - detail.getOrderedQty();
                boolean update = itemDAO.updateQty(temp.getItemCode(), newQty);
                if (!update) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public OrderDTO searchOrder(String id) throws SQLException, ClassNotFoundException {
        Order search = orderDAO.search(id);
        ArrayList<OrderDetailDTO> allDetails = getAllDetailsById(id);
        return new OrderDTO(search.getOrderId(),search.getCustomerId(),search.getOrderDate(),search.getGrossAmount(),search.getNetTotal(),allDetails);
    }

    @Override
    public boolean ifOrderExist(String id) throws SQLException, ClassNotFoundException {
        return orderDAO.ifOrderExist(id);
    }

    @Override
    public boolean removeOrderDetail(String id) throws SQLException, ClassNotFoundException {
        return orderDetailDAO.remove(id);
    }

    @Override
    public ArrayList<OrderDetailDTO> getAllDetailsById(String id) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> allById = orderDetailDAO.getAllById(id);
        ArrayList<OrderDetailDTO>allDetails=new ArrayList<>();
        for (OrderDetail orderDetail : allById) {
            allDetails.add(new OrderDetailDTO(
                    orderDetail.getOrderId(),orderDetail.getItemCode(),orderDetail.getOrderedQty(),orderDetail.getUnitPrice(),orderDetail.getDiscount()
            ));
        }
        return allDetails;
    }
}
