package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/new_dtb";
    private static final String USER = "root";  // tên mysql
    private static final String PASSWORD = "Phong416ct5x2";  // pass

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}