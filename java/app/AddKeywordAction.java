package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;

import java.sql.*;
import java.util.Map;

public class AddKeywordAction extends ActionSupport {
    private String issueID;
    private String keyword;

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword.trim().replaceAll("[^a-zA-Z0-9 .,?!@#$%&*()_+=-]", "");
    }

    public String getIssueID() {
        return issueID;
    }

    @Override
    public void validate() {
        if (keyword == null || keyword.isEmpty()) {
            addFieldError("keyword", "Keyword can't be empty");
        }
    }

    public String execute() throws Exception {
        String sanitizedKeyword = this.keyword;
        if (issueID != null && sanitizedKeyword != null && !sanitizedKeyword.isEmpty()) {
            try (Connection connection = DBUtil.getConnection()) {
                // 1. Check if the keyword already exists
                String sql = "SELECT keywordID FROM Keyword WHERE keyword = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, sanitizedKeyword);
                ResultSet resultSet = statement.executeQuery();

                int keywordID;
                if (resultSet.next()) {
                    // Keyword already exists, get its ID
                    keywordID = resultSet.getInt("keywordID");
                } else {
                    // Insert a new record into the Keyword table and get its ID
                    sql = "INSERT INTO Keyword (keyword) VALUES (?)";
                    statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, sanitizedKeyword);
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (resultSet.next()) {
                        keywordID = resultSet.getInt(1);
                    } else {
                        throw new SQLException("Creating keyword failed, no ID obtained.");
                    }
                }

                // 2. Check if this keyword is already associated with this issue
                sql = "SELECT issueID FROM IssueKeyword WHERE issueID = ? AND keywordID = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, this.issueID);
                statement.setInt(2, keywordID);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // This keyword is already associated with the issue
                    addFieldError("keyword", "This keyword already exists for the issue");
                    return INPUT;
                } else {
                    // 3. Associate the keyword with the issue
                    sql = "INSERT INTO IssueKeyword (issueID, keywordID) VALUES (?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, this.issueID);
                    statement.setInt(2, keywordID);
                    statement.executeUpdate();
                }
            }
        } else {
            addFieldError("keyword", "Keyword can't be empty or null");
            return INPUT;
        }

        return SUCCESS;
    }
}
