package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewKnowledgebaseAction extends ActionSupport {
    private String issueID;
    private IssueBean issue;

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
                    CommentBean commentBean = new CommentBean();
                    commentBean.setComment(resultSet.getString("comment"));
                    commentBean.setDateTimePosted(resultSet.getString("dateTimePosted"));
                    commentBean.setUsername(resultSet.getString("username"));
                    issue.addComment(commentBean);
                }
            }

        }
        return SUCCESS;
    }

}
