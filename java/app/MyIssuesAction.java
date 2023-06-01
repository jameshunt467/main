package app;

import com.opensymphony.xwork2.ActionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.opensymphony.xwork2.Action.SUCCESS;

public class MyIssuesAction {

    ArrayList<IssueBean> issueList;

    public MyIssuesAction() {
        issueList = new ArrayList<>();
    }

    public ArrayList<IssueBean> getIssueList() {
        return issueList;
    }


    public String execute() throws Exception {

        try (Connection connection = DBUtil.getConnection()) {

            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

//            TODO CHANGE BELOW TO SEARCH FOR PROPER ITEMS
            String sql = "SELECT i.* FROM Issue i JOIN UserIssue ui ON i.issueID = ui.issueID WHERE ui.username = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());

            ResultSet resultSet = statement.executeQuery();

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
                issueList.add(issue);
            }
        }

        return SUCCESS;
    }

}
