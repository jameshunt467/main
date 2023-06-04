package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// Class for updating the status of an issue
public class UpdateStatusAction extends ActionSupport {

    private int issueID;
    private String newStatus;

    // Getters and setters
    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }


    // Method for creating a notification
    private void createNotification(Connection connection, String issueID) throws SQLException {
        String sql = "SELECT username FROM UserIssue WHERE issueID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, issueID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String otherUsername = resultSet.getString("username");

                    // Create the notification
                    sql = "INSERT INTO Notification (issueID, username, hasSeen, message, dateTimeSent) VALUES (?, ?, 0, ?, CURRENT_TIMESTAMP)";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, issueID);
                        stmt.setString(2, otherUsername);
                        stmt.setString(3, "Issue status changed to Waiting on Reporter");
                        stmt.executeUpdate();
                    }
                }
            }
        }
    }

    // Executes the status update action
    public String execute() throws Exception {
        try (Connection connection = DBUtil.getConnection()) {

            // If the new status is 'Not Accepted', it's changed to 'In Progress'
            if ("Not Accepted".equals(newStatus)) {
                newStatus = "In Progress";
            }

            if ("Resolved".equals(newStatus)) {
                // If the new status is 'Resolved', we also update the 'dateTimeResolved' field
                String sql = "UPDATE Issue SET status = ?, dateTimeResolved = ? WHERE issueID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newStatus);
                    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    statement.setInt(3, issueID);

                    // Executes the update
                    statement.executeUpdate();
                }
            } else {
                // For other statuses, we only update the 'status' field
                String sql = "UPDATE Issue SET status = ? WHERE issueID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newStatus);
                    statement.setInt(2, issueID);

                    // Executes the update
                    statement.executeUpdate();
                }

                // If the new status is 'Waiting on Reporter', create a notification
                if ("Waiting on Reporter".equals(newStatus)) {
                    createNotification(connection, Integer.toString(issueID));
                }
            }

        }

        return SUCCESS;
    }
}
