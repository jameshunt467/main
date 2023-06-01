package app;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import javax.sql.*;
import java.sql.*;

public class AddCommentAction extends ActionSupport {
    private String issueID;
    private String comment;
    private String username; // assuming you will get the username from the session or elsewhere

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssueID() {
        return issueID;
    }

    // pseudocode for when a comment is added (either by the student or staff logged in)
    // 
    // We have to check UserIssue
    // SELECT * FROM UserIssue WHERE issueID = issueID && username != user.username
    // Pull out this username (this is either the student or staff associated with this issue)
    // CREATE NEW notification with issueID and username


    public String execute() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\MSSQLEXPRESS;databaseName=seng2050_test", "user1", "comp1140isBAE")) {
            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");
            this.username = user.getUsername();

            System.out.println(user);

             // Step 1: Add the new comment to the database
            String sql = "INSERT INTO Comment (comment, dateTimePosted, username, issueID) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, comment);
            statement.setString(2, username);
            statement.setString(3, issueID);
            statement.executeUpdate();  // not checking if this works??

            // Step 2: Notify the other party(s) involved in the issue
            sql = "SELECT * FROM UserIssue WHERE issueID = ? AND username != ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, issueID);
            statement.setString(2, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String otherUsername = resultSet.getString("username");
    
                    // Step 3: Create the notification
                    String message = "New comment added: " + comment;
                    sql = "INSERT INTO Notification (issueID, username, hasSeen, message, dateTimeSent) VALUES (?, ?, 0, ?, CURRENT_TIMESTAMP)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, issueID);
                    statement.setString(2, otherUsername);
                    statement.setString(3, message);
                    statement.executeUpdate();
                }
            }
        }
        return SUCCESS;
    }
}
