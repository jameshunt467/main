package app;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
    private static DataSource dataSource;

    static {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/dbConnection");
        } catch (NamingException e) {
            // Log error or do something with exception
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
