package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;

public class BrowseKnowledgebaseAction extends ActionSupport {

    ArrayList<IssueBean> issueList = new ArrayList<IssueBean>();

    public ArrayList<IssueBean> getIssueList() {
        return issueList;
    }

    public void setIssueList(ArrayList<IssueBean> issueList) {
        this.issueList = issueList;
    }


    public String execute() throws Exception {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\MSSQLEXPRESS;databaseName=seng2050_test", "user1", "comp1140isBAE")) {

            String sql = "SELECT i.* FROM Issue i JOIN KnowledgeBaseArticle k ON i.issueID = k.issueID";

            PreparedStatement statement = connection.prepareStatement(sql);

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
