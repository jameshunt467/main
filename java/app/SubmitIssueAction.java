package app;

import com.opensymphony.xwork2.ActionSupport;

import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;

// Conains the logic for processing form submission

public class SubmitIssueAction extends ActionSupport {

    private String issueDescription;

    // getter and setter for issueDescription

    @Override
    public String execute() throws Exception {

        try {
            // load JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // create connection
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/assignment2jdbc", "sa", "P@ssword!");

            // create a statement
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Issue (description) VALUES (?)");

            stmt.setString(1, issueDescription);

            // execute the query
            stmt.executeUpdate();

            // close the connection
            con.close();

            // return success
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

        // Oldschool way

        // String url = "jdbc:mysql://127.0.0.1/assignment2jdbc";
        // String username = "sa";
        // String password = "P@ssword!";
    
        // try (Connection connection = DriverManager.getConnection(url, username, password)) {
        //     String sql = "INSERT INTO Issue (description) VALUES (?)";
            
        //     try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        //         preparedStatement.setString(1, issueDescription);
        //         preparedStatement.executeUpdate();
        //     }
        // }
    
        // return SUCCESS;
    }
    
}
