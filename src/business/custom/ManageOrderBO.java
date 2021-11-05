package business.custom;

import business.SuperBO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Order;
import entity.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ManageOrderBO extends SuperBO {

    ArrayList<ItemDTO> getAllItems()throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String code)throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String id)throws SQLException, ClassNotFoundException;

    boolean removeOrder (String id)throws SQLException, ClassNotFoundException;

    boolean updateOrder (OrderDTO orderDTO)throws SQLException, ClassNotFoundException;

    OrderDTO searchOrder(String id) throws SQLException, ClassNotFoundException;

    boolean ifOrderExist(String id) throws SQLException, ClassNotFoundException;

    boolean removeOrderDetail(String id) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDetailDTO> getAllDetailsById(String id) throws SQLException, ClassNotFoundException;
}
