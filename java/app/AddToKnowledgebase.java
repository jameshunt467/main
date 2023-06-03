package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class AddToKnowledgebase extends ActionSupport {

    public int issueID;
    private String resolutionDetails;
    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }
    public String getResolutionDetails() { return this.resolutionDetails; }
    public void setResolutionDetails(String resolutionDetails) { this.resolutionDetails = resolutionDetails; }

    public String execute() throws Exception {

        try (Connection connection = DBUtil.getConnection()) {
            // first, insert the new keyword and get its ID
            String sql = "INSERT INTO KnowledgeBaseArticle (issueID) VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(issueID));
            statement.executeUpdate();

            sql = "UPDATE Issue SET resolutionDetails = ? WHERE issueID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, this.resolutionDetails);
            statement.setString(2, String.valueOf(issueID));

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

}

