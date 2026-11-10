package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection conn;

    private static String url = "jdbc:mysql://127.0.0.1:3307/testTPP";
    private static String user = "root";
    private static String password = "root1234";

    public static Connection getConnection()

    {
        if(conn == null)
        {
            try {
                conn = DriverManager.getConnection(url,user,password);
            }
            catch (SQLException e)
            {
                e.getMessage();
            }
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
