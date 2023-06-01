package app;

import com.opensymphony.xwork2.ActionContext;
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
            case "Category":
                issueList.sort(Comparator.comparing(IssueBean::getCategory, Comparator.nullsLast(String::compareTo)));
                break;
            case "Status":
                issueList.sort(Comparator.comparing(IssueBean::getStatus, Comparator.nullsLast(String::compareTo)));
                break;
            case "description":
                issueList.sort(Comparator.comparing(IssueBean::getDescription, Comparator.nullsLast(String::compareTo)));
                break;
            case "Reporting Time":
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeReported, Comparator.nullsLast(String::compareTo)).reversed());
                break;
            case "Time in Progress":
//                TODO THIS
                break;
            default:
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeReported, Comparator.nullsLast(String::compareTo)).reversed());
                break;
        }

    }

    public String execute() throws Exception {

        UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

        if (user == null || !user.getRole().equals("staff")) {
            return ERROR;
        }

        try (Connection connection = DBUtil.getConnection()) {

//            TODO CHANGE BELOW TO SEARCH FOR KEYWORD ITEMS
            String sql = "SELECT * FROM Issue WHERE title LIKE ? OR description LIKE ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");

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

        resortArray();

        return SUCCESS;
    }
}
