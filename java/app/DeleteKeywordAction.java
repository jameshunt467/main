package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteKeywordAction extends ActionSupport {
    private int keywordID; // The ID of the keyword to delete
    private int issueID; // The ID of the issue to delete the keyword from

    // Getters
    public int getKeywordID() { return keywordID; }
    public int getIssueID() { return issueID; }

    // Setters
    public void setKeywordID(int keywordID) { this.keywordID = keywordID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }

    public String execute() {
        // Try-with-resources block for automatic resource management
        try (Connection connection = DBUtil.getConnection()) {
            // SQL query to delete a record from the IssueKeyword table
            String sql = "DELETE FROM IssueKeyword WHERE keywordID = ? AND issueID = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, keywordID);
            stmt.setInt(2, issueID);

            // Execute the update operation
            stmt.executeUpdate();

            // Everything went well, so return success
            return SUCCESS;

        } catch(SQLException e) {
            // Print stack trace for debugging
            e.printStackTrace();
            // Something went wrong, so return error
            return ERROR;
        }
    }
}
