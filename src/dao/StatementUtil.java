package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatementUtil {

    private static PreparedStatement getPreparedStatement(String query,Object...args) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i+1,args[i]);
        }
        return statement;
    }

    public static boolean executeUpdate(String query,Object...args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(query, args).executeUpdate()>0;
    }

    public static ResultSet executeQuery(String query,Object...args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(query, args).executeQuery();
    }
}
