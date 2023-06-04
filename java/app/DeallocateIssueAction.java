package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeallocateIssueAction extends BaseAction {
    private int issueID;
    private String username;

    // Getters
    public int getIssueID() { return this.issueID; }
    public String getUsername() { return this.username; }

    // Setters
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public void setUsername(String username) { this.username = username; }

    public String execute() {
        // Try-with-resources block for automatic resource management
        try (Connection connection = DBUtil.getConnection()) {
            // SQL query to delete a record from the StaffIssue table
            String sql = "DELETE FROM StaffIssue WHERE username = ? AND issueID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setInt(2, issueID);

            // Execute the update operation and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            // If any row has been affected, return success, otherwise return error
            return rowsAffected > 0 ? "success" : "error";

        } catch(SQLException e) {
            // Print stack trace for debugging
            e.printStackTrace();
            // Return error if an exception has occurred
            return "error";
        }
    }
}
