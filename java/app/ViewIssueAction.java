package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewIssueAction extends ActionSupport {
    private String issueID;
    private IssueBean issue;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public IssueBean getIssue() {
        return issue;
    }

    public String execute() throws Exception {
        try (Connection connection = DBUtil.getConnection()) {

            String sql = "SELECT * FROM Issue WHERE issueID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, issueID);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                issue = new IssueBean();
                issue.setIssueID(resultSet.getString("issueID"));
                issue.setTitle(resultSet.getString("title"));
                issue.setCategory(resultSet.getString("category"));
                issue.setStatus(resultSet.getString("status"));
                issue.setDescription(resultSet.getString("description"));
                issue.setResolutionDetails(resultSet.getString("resolutionDetails"));
                issue.setDateTimeReported(resultSet.getString("dateTimeReported"));
                issue.setDateTimeResolved(resultSet.getString("dateTimeResolved"));

                sql = "SELECT * FROM Comment WHERE issueID = ?";

                statement = connection.prepareStatement(sql);
                statement.setString(1, issueID);

                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    issue.addComment(resultSet.getString("comment"));
                }

                sql = "SELECT k.keyword FROM IssueKeyword ik JOIN Keyword k ON ik.keywordID = k.keywordID WHERE ik.issueID = ?";

                statement = connection.prepareStatement(sql);
                statement.setString(1, issueID);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    issue.addKeyword(resultSet.getString("keyword"));
                }

                if(keyword != null && !keyword.isEmpty()) {
                    sql = "INSERT INTO Keyword (keyword, issueID) VALUES (?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, keyword);
                    statement.setString(2, issueID);
                    statement.executeUpdate();
                }

            }

        }
        return SUCCESS;
    }

}
