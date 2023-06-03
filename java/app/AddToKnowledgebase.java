package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddToKnowledgebase extends ActionSupport {
    public int issueID;
    private String resolutionDetails;

    // Setters and getters
    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public String getResolutionDetails() {
        return this.resolutionDetails;
    }

    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

    public String execute() {
        try (Connection connection = DBUtil.getConnection()) {
            // Step 1: Insert the new keyword and get its ID
            String sql = "INSERT INTO KnowledgeBaseArticle (issueID) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, issueID);
            statement.executeUpdate();

            // Step 2: Update the resolution details for the issue
            sql = "UPDATE Issue SET resolutionDetails = ? WHERE issueID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, this.resolutionDetails);
            statement.setInt(2, issueID);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database error while processing the keyword: " + e.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }
}
