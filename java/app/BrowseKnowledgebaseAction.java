package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;

import com.opensymphony.xwork2.ActionSupport;

public class BrowseKnowledgebaseAction extends ActionSupport {

    ArrayList<IssueBean> issueList;
    private String sort;
    private String search = "";

    public BrowseKnowledgebaseAction() {
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
                issueList.sort(Comparator.comparing(IssueBean::getIssueID, Comparator.nullsLast(String::compareTo)));
                break;
            case "title":
                issueList.sort(Comparator.comparing(IssueBean::getTitle, Comparator.nullsLast(String::compareTo)));
                break;
            case "category":
                issueList.sort(Comparator.comparing(IssueBean::getCategory, Comparator.nullsLast(String::compareTo)));
                break;
            case "status":
                issueList.sort(Comparator.comparing(IssueBean::getStatus, Comparator.nullsLast(String::compareTo)));
                break;
            case "description":
                issueList.sort(Comparator.comparing(IssueBean::getDescription, Comparator.nullsLast(String::compareTo)));
                break;
            case "resolutionDetails":
                issueList.sort(Comparator.comparing(IssueBean::getResolutionDetails, Comparator.nullsLast(String::compareTo)));
                break;
            case "dateTimeReported":
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeReported, Comparator.nullsLast(String::compareTo)));
                break;
            case "dateTimeResolved":
                issueList.sort(Comparator.comparing(IssueBean::getDateTimeResolved, Comparator.nullsLast(String::compareTo)));
                break;
            default:
                issueList.sort(Comparator.comparing(IssueBean::getIssueID, Comparator.nullsLast(String::compareTo)));
                break;
        }

    }


    public String execute() throws Exception {

        try (Connection connection = DBUtil.getConnection()) {

            String sql = "SELECT i.* FROM Issue i JOIN KnowledgeBaseArticle k ON i.issueID = k.issueID WHERE i.title LIKE ? OR i.description LIKE ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + this.search + "%");
            statement.setString(2, "%" + this.search + "%");

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
