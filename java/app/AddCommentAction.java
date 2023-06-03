package app;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCommentAction extends ActionSupport {
    private String issueID;
    private String comment;
    private String username;

    // Setter for issueID
    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    // Setter for comment, sanitizes the input and checks for changes
    public void setComment(String comment) {
        String sanitizedComment = comment.replaceAll("[^a-zA-Z0-9. ,?!@#$%&*()_+=-]", "");
        if (!comment.equals(sanitizedComment)) {
            ActionContext.getContext().getSession().put("commentError", "Could not add comment, please remove special characters");
        } else {
            this.comment = sanitizedComment;
        }
    }

    // Getters for issueID, comment, and username
    public String getIssueID() {
        return issueID;
    }
    public String getComment() {
        return comment;
    }
    public String getUsername() {
        return username;
    }

    // The execute method for the action, handling the addition of a comment and notification creation
    public String execute() throws Exception {
        // Check for comment error
        if (ActionContext.getContext().getSession().containsKey("commentError")) {
            return ERROR;
        }
        try (Connection connection = DBUtil.getConnection()) {
            // Retrieve user from the session
            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");
            this.username = user.getUsername();

            // Step 1: Add the new comment to the database
            try {
                String sql = "INSERT INTO Comment (comment, dateTimePosted, username, issueID) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, comment);
                statement.setString(2, username);
                statement.setString(3, issueID);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting comment into the database: " + e.getMessage());
                return ERROR;
            }

            // Step 2: Notify the other parties involved in the issue
            String sql = "SELECT * FROM UserIssue WHERE issueID = ? AND username != ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, issueID);
            statement.setString(2, username);

            // Execute query and process results
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
            } catch (SQLException e) {
                System.err.println("Error creating notification: " + e.getMessage());
                return ERROR;
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }
}
