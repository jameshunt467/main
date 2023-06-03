package app;

import com.opensymphony.xwork2.ActionSupport;
import app.BaseAction;
import app.IssueBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewIssueAction extends BaseAction {
    private List<String> staffMembers = new ArrayList<>();
    private String currentlyAssigned = null;
    private boolean isManager = false;

    private int issueID;
    private IssueBean issue;
    private String keyword;

    public String getKeyword() { return keyword; }
    public int getIssueID() { return this.issueID; }
    public IssueBean getIssue() { return issue; }
    public String getCurrentlyAssigned() { return this.currentlyAssigned; }
    public List<String> getStaffMembers() { return staffMembers; }
    public boolean isManager() { return this.isManager; }

    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public void setIssue(IssueBean issue) { this.issue = issue; }
    public void setIsManager(boolean state) { this.isManager = state; }

    public String execute() throws Exception {
        handleSessionErrors();

        try (Connection connection = DBUtil.getConnection()) {
            checkUserAndFetchAssignedStaff(connection);
            fetchIssueDetails(connection);
        }

        return SUCCESS;
    }

    private void handleSessionErrors() {
        handleSessionError("commentError", "comment");
        handleSessionError("error", "keyword");
    }

    private void handleSessionError(String sessionKey, String field) {
        if (session.containsKey(sessionKey)) {
            String errorMessage = (String) session.get(sessionKey);
            this.addFieldError(field, errorMessage);
            session.remove(sessionKey);
        }
    }

    private void checkUserAndFetchAssignedStaff(Connection connection) throws SQLException {
        String sql = "SELECT managerFlag FROM Staff WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, getLoggedInUser().getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    this.isManager = rs.getBoolean("managerFlag");
                    ((StaffBean)getLoggedInUser()).setManager(isManager);
                    fetchStaffMembers(connection);
                }
            }
        }
    }

    private void fetchStaffMembers(Connection connection) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;


        String sql = "SELECT username FROM Staff";

        stmt = connection.prepareStatement(sql);
        rs = stmt.executeQuery();

        while (rs.next()) {
            staffMembers.add(rs.getString("username"));
        }

        // Fetch the current assigned user for this issue
        sql = "SELECT username FROM StaffIssue WHERE issueID = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, issueID);
        rs = stmt.executeQuery();

        if(rs.next()) {
            currentlyAssigned = rs.getString("username");
        }

        rs.close();
        stmt.close();
    }

    private void fetchIssueDetails(Connection connection) throws SQLException {
        String sql = "SELECT * FROM Issue WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    setIssueDetails(rs);
                    issue.setInKnowledgeBase(checkIfInKnowledgeBase(connection));
                    fetchComments(connection);
                    fetchKeywords(connection);
                    insertKeyword(connection);
                }
            }
        }
    }

    private void setIssueDetails(ResultSet rs) throws SQLException {
        issue = new IssueBean();
        issue.setIssueID(rs.getString("issueID"));
        issue.setTitle(rs.getString("title"));
        issue.setCategory(rs.getString("category"));
        issue.setStatus(rs.getString("status"));
        issue.setDescription(rs.getString("description"));
        issue.setResolutionDetails(rs.getString("resolutionDetails"));
        issue.setDateTimeReported(rs.getString("dateTimeReported"));
        issue.setDateTimeResolved(rs.getString("dateTimeResolved"));
    }

    private void fetchComments(Connection connection) throws SQLException {
        String sql = "SELECT * FROM Comment WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CommentBean commentBean = new CommentBean();
                    commentBean.setComment(rs.getString("comment"));
                    commentBean.setDateTimePosted(rs.getString("dateTimePosted"));
                    commentBean.setUsername(rs.getString("username"));
                    issue.addComment(commentBean);
                }
            }
        }
    }

    private boolean checkIfInKnowledgeBase(Connection connection) throws SQLException {
        String sql = "SELECT issueID FROM KnowledgeBaseArticle WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void fetchKeywords(Connection connection) throws SQLException {
        String sql = "SELECT k.keywordID, k.keyword FROM IssueKeyword ik JOIN Keyword k ON ik.keywordID = k.keywordID WHERE ik.issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KeywordBean keywordBean = new KeywordBean();
                    keywordBean.setKeywordID(rs.getString("keywordID"));
                    keywordBean.setKeyword(rs.getString("keyword"));
                    issue.addKeyword(keywordBean);
                }
            }
        }
    }

    private void insertKeyword(Connection connection) throws SQLException {
        if(keyword != null && !keyword.isEmpty()) {
            String sql = "INSERT INTO Keyword (keyword) VALUES (?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                stmt.executeUpdate();
            }
        }
    }
}
