package business.custom.impl;

import business.custom.CashierDashBoardBO;
import dao.DAOFactory;
import dao.custom.QueryDAO;

import java.sql.SQLException;

public class CashierDashBoardBOImpl implements CashierDashBoardBO {
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);
    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        return queryDAO.getItemCount();
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
       return queryDAO.getCustomerCount();
    }

    @Override
    public int getDailyOrdersCount(String date) throws SQLException, ClassNotFoundException {
        return queryDAO.getDailyOrdersCount(date);
    }
}
