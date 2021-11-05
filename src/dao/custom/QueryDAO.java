package dao.custom;

import dao.SuperDAO;

import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {
    int getItemCount()throws SQLException, ClassNotFoundException;

    int getCustomerCount()throws SQLException, ClassNotFoundException;

    int getDailyOrdersCount(String date)throws SQLException, ClassNotFoundException;
}
