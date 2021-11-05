package business.custom.impl;

import business.custom.AllOrdersBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Order;
import entity.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public class AllOrdersBOImpl implements AllOrdersBO {

    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetail = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<Order> all = orderDAO.getAll();
        ArrayList<OrderDTO>allOrders=new ArrayList<>();
        for (Order order : all) {
            ArrayList<OrderDetail> allById = orderDetail.getAllById(order.getOrderId());
            ArrayList<OrderDetailDTO> allDetails=new ArrayList<>();
            for (OrderDetail detail : allById) {
                allDetails.add(new OrderDetailDTO(
                        detail.getOrderId(),
                        detail.getItemCode(),
                        detail.getOrderedQty(),
                        detail.getUnitPrice(),
                        detail.getDiscount()
                ));
            }
            allOrders.add(new OrderDTO(
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getGrossAmount(),
                    order.getNetTotal(),
                    allDetails
            ));
        }
        return allOrders;
    }
}
