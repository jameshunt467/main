package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisplayDataAction {

    private String data;

    public String execute() {
        try (Connection connection = DBUtil.getConnection()) {
            // Execute SQL queries to fetch data from the database
            // For example:
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT comment FROM Comment");

            if (resultSet.next()) {
                // Retrieve the data from the result set
                data = resultSet.getString("comment");
            }
        } catch (SQLException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    public String getData() {
        return data;
    }
}