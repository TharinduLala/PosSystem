package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO <T,ID>extends SuperDAO {

    boolean add(T t) throws SQLException,ClassNotFoundException;

    boolean remove(ID id) throws SQLException,ClassNotFoundException;

    boolean update(T t) throws SQLException,ClassNotFoundException;

    ArrayList<T> getAll() throws SQLException,ClassNotFoundException;

    T search (ID id) throws SQLException,ClassNotFoundException;
}
