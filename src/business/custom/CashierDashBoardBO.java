package business.custom;

import business.SuperBO;

import java.sql.SQLException;

public interface CashierDashBoardBO extends SuperBO {

    int getItemCount() throws SQLException, ClassNotFoundException;

    int getCustomerCount() throws SQLException, ClassNotFoundException;

    int getDailyOrdersCount(String date) throws SQLException, ClassNotFoundException;
}
