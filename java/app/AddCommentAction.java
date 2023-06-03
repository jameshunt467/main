package app;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import javax.sql.*;
import java.sql.*;

public class AddCommentAction extends ActionSupport {
    private String issueID;
    private String comment;
    private String username; // assuming you will get the username from the session or elsewhere

    private String dateTimePosted;

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setComment(String comment) {
        // sanitize the comment input to disallow certain special characters
        // This will replace any special character not in the list with an empty string
        String sanitizedComment = comment.replaceAll("[^a-zA-Z0-9 .,?!@#$%&*()_+=-]", "");

        // Check if the comment has been changed, if so add error message
        if (!comment.equals(sanitizedComment)) {
            // Put the error message into the session
            ActionContext.getContext().getSession().put("commentError", "Could not add comment, please remove special characters");
            // this.addFieldError("comment", "Could not add comment, please remove special characters");
        } else {
            this.comment = sanitizedComment;
        }
    }

    public String getIssueID() {
        return issueID;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public String getDateTimePosted() {
        return dateTimePosted;
    }



    // pseudocode for when a comment is added (either by the student or staff logged in)
    //
    // We have to check UserIssue
    // SELECT * FROM UserIssue WHERE issueID = issueID && username != user.username
    // Pull out this username (this is either the student or staff associated with this issue)
    // CREATE NEW notification with issueID and username


    public String execute() throws Exception {
        // The user may have tried to add a comment with special characters
        if (ActionContext.getContext().getSession().containsKey("commentError")) {
            return ERROR;
        }
        
        try (Connection connection = DBUtil.getConnection()) {
            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");
            this.username = user.getUsername();

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
