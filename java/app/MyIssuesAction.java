package app;

import com.opensymphony.xwork2.ActionContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import static com.opensymphony.xwork2.Action.SUCCESS;

public class MyIssuesAction {

    // Use List interface for flexibility
    private ArrayList<IssueBean> issueList;

    public MyIssuesAction() {
        issueList = new ArrayList<>();
    }

    // Returns the list of IssueBeans
    public ArrayList<IssueBean> getIssueList() {
        return issueList;
    }

    public String execute() {
        // Use try-with-resources to manage resources and ensure they are closed
        try (Connection connection = DBUtil.getConnection()) {

            // Fetch the user from the session
            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

            // Select SQL based on the user's role
            String sql;
            if (!Objects.equals(user.getRole(), "staff")) {
                sql = "SELECT i.* FROM Issue i JOIN UserIssue ui ON i.issueID = ui.issueID WHERE ui.username = ?";
            } else {
                sql = "SELECT i.* FROM Issue i JOIN StaffIssue ui ON i.issueID = ui.issueID WHERE ui.username = ?";
            }

            // Create a PreparedStatement to avoid SQL injection
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Loop through each row of result set and create an IssueBean object
            while (resultSet.next()) {
                IssueBean issue = new IssueBean();
                issue.setIssueID(resultSet.getString("issueID"));
                issue.setTitle(resultSet.getString("title"));
                issue.setCategory(resultSet.getString("category"));
                issue.setStatus(resultSet.getString("status"));
                issue.setDescription(resultSet.getString("description"));
                issue.setResolutionDetails(resultSet.getString("resolutionDetails"));
                issue.setDateTimeReported(resultSet.getString("dateTimeReported"));
                issue.setDateTimeResolved(resultSet.getString("dateTimeResolved"));

                // Add each issue to the list
                issueList.add(issue);
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions here
            e.printStackTrace();
            return "error";
        }

        // Return success if everything is okay
        return SUCCESS;
    }

}
