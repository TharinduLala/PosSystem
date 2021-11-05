package business.custom;

import business.SuperBO;
import dto.OrderDTO;


import java.sql.SQLException;
import java.util.ArrayList;

public interface AllOrdersBO extends SuperBO {

    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;
}
