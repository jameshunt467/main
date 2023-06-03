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

            String sql = "SELECT i.*, k.keyword, k.keywordID FROM Issue i LEFT JOIN issueKeyword ik ON i.issueID = ik.issueID LEFT JOIN keyword k ON ik.keywordID = k.keywordID WHERE i.title LIKE ? OR i.description LIKE ? OR k.keyword LIKE ? ORDER BY i.issueID";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + this.search + "%");
            statement.setString(2, "%" + this.search + "%");
            statement.setString(3, "%" + this.search + "%");

            ResultSet resultSet = statement.executeQuery();

            String lastIssueID = "";
            IssueBean issue = null;
            while (resultSet.next()) {
                String currentIssueID = resultSet.getString("issueID");
                if (!currentIssueID.equals(lastIssueID)) {
                    if (issue != null) {
                        issueList.add(issue);
                    }
                    issue = new IssueBean();
                    issue.setIssueID(currentIssueID);
                    issue.setTitle(resultSet.getString("title"));
                    issue.setCategory(resultSet.getString("category"));
                    issue.setStatus(resultSet.getString("status"));
                    issue.setDescription(resultSet.getString("description"));
                    issue.setResolutionDetails(resultSet.getString("resolutionDetails"));
                    issue.setDateTimeReported(resultSet.getString("dateTimeReported"));
                    issue.setDateTimeResolved(resultSet.getString("dateTimeResolved"));
                }
                KeywordBean keywordBean = new KeywordBean();
                keywordBean.setKeywordID(resultSet.getString("keywordID"));
                keywordBean.setKeyword(resultSet.getString("keyword"));
                issue.addKeyword(keywordBean);
                lastIssueID = currentIssueID;
            }
            if (issue != null) {
                issueList.add(issue);
            }
        }

        resortArray();

        return SUCCESS;
    }

}

