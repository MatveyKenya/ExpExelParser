package Util;

import Entity.Order;
import Repository.OrderRepository;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DbConnector {
    final private String dbUser = "postgres";
    final private String dbPassword ="0123";
    final private String url = "jdbc:postgresql://localhost:5432/elf";
    final private Connection con;
    final private Statement st;

    public static void main(String[] args) {

        DbConnector db = null;
        try {
            db = new DbConnector();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        assert db != null;

        OrderRepository orderRepo = new OrderRepository(db);
        System.out.println("Подключение есть");
        var list = orderRepo.getOrderAll();
        list.forEach(System.out::println);
        db.closeConnection();

    }

    //конструктор
    public DbConnector() throws SQLException {
        con = DriverManager.getConnection(url, dbUser, dbPassword);
        st = con.createStatement();
    }

    /**
     * {Create - Update - Delete} execute Query
     * @param query
     * @return 1 и более - успех, 0 ничего, -1 ошибка
     */
    public int execute(String query){
        int result;
        try {
            result = st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            result = -1; // -1 значит ошибка
        }
        return result;
    }

    /**
     * for SELECT query
      * @param query - запрос
     * @return ResultSet
     */
    public ResultSet executeResult(String query) throws SQLException {
        return st.executeQuery(query);
    }

    /**
     * закрытие соединений если по правильному в конце работы
     */
    public void closeConnection(){
        try {
            st.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
