package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;


public class BrowseIssuesAction extends ActionSupport {
    ArrayList<IssueBean> issueList;
    private String sort;
    private String search = "";

    public BrowseIssuesAction() {
        issueList = new ArrayList<>();
    }

    public ArrayList<IssueBean> getIssueList() {
        return issueList;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setSearchTerm(String searchTerm) {
        this.search = searchTerm;
    }

    public void resortArray() {
        if (sort == null) {
            sort = "issueID";
        }

        switch (sort) {
            case "issueID":
                issueList.sort(Comparator.comparing(IssueBean::getIssueID));
                break;
            case "title":
                issueList.sort(Comparator.comparing(IssueBean::getTitle));
                break;
            case "category":
                issueList.sort(Comparator.comparing(IssueBean::getCategory));
                break;
            case "status":
                issueList.sort(Comparator.comparing(IssueBean::getStatus));
                break;
            case "description":
                issueList.sort(Comparator.comparing(IssueBean::getDescription));
                break;
            case "resolutionDetails":
                issueList.sort(Comparator.comparing(IssueBean::getResolutionDetails));
                break;
            case "dateTimeReported":
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeReported));
                break;
            case "dateTimeResolved":
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeResolved));
                break;
            default:
                issueList.sort(Comparator.comparing(IssueBean::getIssueID));
                break;
        }
    }

    public String execute() throws Exception {


        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\MSSQLEXPRESS;databaseName=seng2050_test", "user1", "comp1140isBAE")) {

//            TODO CHANGE BELOW TO SEARCH FOR PROPER ITEMS
            String sql = "SELECT * FROM Issue WHERE title LIKE ? OR description LIKE ?";

            System.out.println("SQL: " + sql);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");

            ResultSet resultSet = statement.executeQuery();

            System.out.println("Result Set: " + resultSet);

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

        resortArray();

        return SUCCESS;
    }
}
