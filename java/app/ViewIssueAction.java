package app;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Class for viewing details of a specific issue
public class ViewIssueAction extends BaseAction {

    private List<String> staffMembers = new ArrayList<>();
    private String currentlyAssigned = null;
    private boolean isManager = false;

    private int issueID;
    private IssueBean issue;
    private String keyword;

    // Getters and setters
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

    // Executes the view issue action
    public String execute() throws Exception {
        // Handling session errors
        handleSessionErrors();

        try (Connection connection = DBUtil.getConnection()) {
            // Checking the role of the user and fetching assigned staff
            checkUserAndFetchAssignedStaff(connection);
            // Fetching the details of the issue
            fetchIssueDetails(connection);
        }

        return SUCCESS;
    }

    private void handleSessionErrors() {
        // Handling errors for 'comment' and 'keyword' fields
        handleSessionError("commentError", "comment");
        handleSessionError("error", "keyword");
    }

    private void handleSessionError(String sessionKey, String field) {
        // If there's an error in the session for the given field, we add a field error and remove it from the session
        if (session.containsKey(sessionKey)) {
            String errorMessage = (String) session.get(sessionKey);
            this.addFieldError(field, errorMessage);
            session.remove(sessionKey);
        }
    }

    private void checkUserAndFetchAssignedStaff(Connection connection) throws SQLException {
        // Check if the user is a manager and fetch the staff members
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
        // Fetch the staff members and the staff member currently assigned to the issue
        String sql = "SELECT username FROM Staff";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffMembers.add(rs.getString("username"));
            }
        }

        // Fetch the current assigned user for this issue
        sql = "SELECT username FROM StaffIssue WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rsStaffIssue = stmt.executeQuery()) {
                if(rsStaffIssue.next()) {
                    currentlyAssigned = rsStaffIssue.getString("username");
                }
            }
        }
    }

    private void incrementKnowledgeBaseArticleViewCount(Connection connection) throws SQLException {
        // Increment the view count of the knowledge base article
        String sql = "UPDATE KnowledgeBaseArticle SET viewCount = viewCount + 1 WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);
            stmt.executeUpdate();
        }
    }


    private void fetchIssueDetails(Connection connection) throws SQLException {
        // Fetch the details of the issue
        String sql = "SELECT * FROM Issue WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    setIssueDetails(rs);
                    if(checkIfInKnowledgeBase(connection)) {
                        issue.setInKnowledgeBase(true);
                        incrementKnowledgeBaseArticleViewCount(connection); // Call the method here
                    }
                    fetchComments(connection);
                    fetchKeywords(connection);
                    insertKeyword(connection);
                }
            }
        }
    }


    private void setIssueDetails(ResultSet rs) throws SQLException {
        // Set the issue details
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
        // Fetch the comments for the issue
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
        // Check if the issue is in the knowledge base
        String sql = "SELECT issueID FROM KnowledgeBaseArticle WHERE issueID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueID);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void fetchKeywords(Connection connection) throws SQLException {
        // Fetch the keywords for the issue
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
        // Insert a keyword for the issue
        if(keyword != null && !keyword.isEmpty()) {
            String sql = "INSERT INTO Keyword (keyword) VALUES (?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                stmt.executeUpdate();
            }
        }
    }
}
