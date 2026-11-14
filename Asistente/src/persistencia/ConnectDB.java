package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection conn;

    private static String URL = "jdbc:mysql://localhost:3307/testTPP";
    private static String USER = "root";
    private static String PASSWORD = "root1234";

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }


    public static void closeConnection()
    {
        if(conn != null)
        {
            try {
                conn.close();
            }
            catch (SQLException e)
            {
                e.getMessage();
            }
        }
    }



}
