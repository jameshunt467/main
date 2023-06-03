package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;

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
        // sanitize the comment input to disallow certain special characters
        // This will replace any special character not in the list with an empty string
        String sanitizedKeyword = keyword.replaceAll("[^a-zA-Z0-9 .,?!@#$%&*()_+=-]", "");

        // Check if the comment has been changed, if so add error message
        if (!keyword.equals(sanitizedKeyword)) {
            // Put the error message into the session
            ActionContext.getContext().put("error", "Could not add keyword, please remove special characters");
        } else {
            this.keyword = sanitizedKeyword;
        }
    }

    public String getIssueID() {
        return issueID;
    }

    public String execute() throws Exception {
        // If there are action errors, return ERROR
        // The user may have tried to add a keywprd with special characters
        if (ActionContext.getContext().getSession().containsKey("keywordError")) {
            return ERROR;
        }
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

