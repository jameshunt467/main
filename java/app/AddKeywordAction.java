package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddKeywordAction extends ActionSupport {
    private String issueID;
    private String keyword;

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String execute() throws Exception {
        if (issueID != null && keyword != null && !keyword.isEmpty()) {
            try (Connection connection = DBUtil.getConnection()) {

                // 1. Check if the keyword already exists
                String sql = "SELECT keywordID FROM Keyword WHERE keyword = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, keyword);
                ResultSet resultSet = statement.executeQuery();
                String keywordID;
                if (resultSet.next()) {
                    keywordID = resultSet.getString("keywordID");
                } else {
                    // Keyword doesn't exist, insert it
                    sql = "INSERT INTO Keyword (keyword) OUTPUT INSERTED.keywordID VALUES (?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, keyword);
                    resultSet = statement.executeQuery();
                    resultSet.next();
                    keywordID = resultSet.getString(1);
                }

                // 2. Insert a new record into the IssueKeyword table
                sql = "INSERT INTO IssueKeyword (issueID, keywordID) VALUES (?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, issueID);
                statement.setString(2, keywordID);
                statement.executeUpdate();
            }
        }
        return SUCCESS;
    }
}

