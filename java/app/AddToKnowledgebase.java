package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddToKnowledgebase extends ActionSupport {

    public int issueID;
    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public String execute() throws Exception {

        try (Connection connection = DBUtil.getConnection()) {
            // first, insert the new keyword and get its ID
            String sql = "INSERT INTO KnowledgeBaseArticle (issueID) VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(issueID));
            statement.executeUpdate();

            sql = "UPDATE Issue SET resolutionDetails = ?, dateTimeResolved = ? WHERE issueID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "This issue has been resolved.");
            statement.setString(2, new Date(System.currentTimeMillis()).toString());
            statement.setString(3, String.valueOf(issueID));

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

}

