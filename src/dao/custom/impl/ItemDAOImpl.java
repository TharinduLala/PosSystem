package dao.custom.impl;

import dao.StatementUtil;
import dao.custom.ItemDAO;
import entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item item) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("INSERT INTO Item(itemCode,description,packSize,qtyOnHand,unitPrice) VALUES (?,?,?,?,?)",
                item.getItemCode(),item.getDescription(),item.getPackSize(),item.getQtyOnHand(),item.getUnitPrice());
    }

    @Override
    public boolean remove(String code) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("DELETE FROM Item WHERE itemCode=?",code);
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("UPDATE Item SET description=?,packSize=?,qtyOnHand=?,unitPrice=? WHERE itemCode=?",
                item.getDescription(),item.getPackSize(),item.getQtyOnHand(),item.getUnitPrice(),item.getItemCode());
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Item> allItems=new ArrayList<>();
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM Item");
        while (resultSet.next()) {
            allItems.add(new Item(
                resultSet.getString("itemCode"),
                resultSet.getString("description"),
                resultSet.getString("packSize"),
                resultSet.getInt("qtyOnHand"),
                resultSet.getBigDecimal("unitPrice")
            ));
        }
        return  allItems;
    }

    @Override
    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT * FROM Item WHERE itemCode=?", code);
        resultSet.next();
        return new Item(
                resultSet.getString("itemCode"),
                resultSet.getString("description"),
                resultSet.getString("packSize"),
                resultSet.getInt("qtyOnHand"),
                resultSet.getBigDecimal("unitPrice")
        );
    }

    @Override
    public boolean ifItemExist(String code) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeQuery("SELECT itemCode FROM Item WHERE itemCode=?", code).next();
    }

    @Override
    public String generateNewItemCode() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = StatementUtil.executeQuery("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1;");
        if (resultSet.next()) {
            String lastId = resultSet.getString("itemCode");
            int newItemId = Integer.parseInt(lastId.replace("I", "")) + 1;
            return String.format("I%03d", newItemId);
        } else {
            return "I001";
        }
    }

    @Override
    public boolean updateQty(String code, int qty) throws SQLException, ClassNotFoundException {
        return StatementUtil.executeUpdate("UPDATE Item SET qtyOnHand=? WHERE itemCode=?",qty,code);
    }
}
