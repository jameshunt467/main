package app;

import com.opensymphony.xwork2.ActionSupport;

import app.BaseAction;
import app.IssueBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewIssueAction extends BaseAction {
    private List<String> staffMembers = new ArrayList<>();  // Used for assigning staff members
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
    public void setCurrentlyAssigned(String assigned) { this.currentlyAssigned = assigned; }
    public void setStaffMembers(List<String> sMembers) { this.staffMembers = sMembers; }
    public void setIsManager(boolean state) { this.isManager = state; }

    public String execute() throws Exception {
        // This is for the comment error
        if (session.containsKey("commentError")) {
            String errorMessage = (String) session.get("commentError");
            this.addFieldError("comment", errorMessage);
            session.remove("commentError");
        }
        // This is for the keyword error
        if (session.containsKey("error")) {
            String errorMessage = (String) session.get("error");
            this.addFieldError("keyword", errorMessage);
            session.remove("error");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try (Connection connection = DBUtil.getConnection()) {

            // Checking to see if logged in user is a manager
            String sql = "SELECT managerFlag FROM Staff WHERE username = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, getLoggedInUser().getUsername());
            rs = stmt.executeQuery();
            if(rs.next()) {
                this.isManager = rs.getBoolean("managerFlag");
                if(this.isManager) {
                    ((StaffBean)getLoggedInUser()).setManager(isManager); 
                    sql = "SELECT Staff.username FROM Staff LEFT JOIN UserIssue ON Staff.username = UserIssue.username " +
                        "WHERE (UserIssue.issueID != ? OR UserIssue.issueID IS NULL)";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, issueID);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        staffMembers.add(rs.getString("username"));
                    }
                    // Add the current manager to the list as well, if it's not already there
                    // if (!staffMembers.contains(getLoggedInUser().getUsername())) {
                    //     staffMembers.add(getLoggedInUser().getUsername());
                    // }
                } else {
                    sql = "SELECT username FROM UserIssue WHERE issueID = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, issueID);
                    rs = stmt.executeQuery();
                    if(!rs.next()) {
                        staffMembers.add(getLoggedInUser().getUsername());
                    } 
                }
                // Fetch the current assigned user for this issue
                sql = "SELECT username FROM UserIssue WHERE issueID = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, issueID);
                rs = stmt.executeQuery();
                if(rs.next()) {
                    currentlyAssigned = rs.getString("username");
                }
                rs.close();
                stmt.close();
                this.setStaffMembers(staffMembers);
                this.setCurrentlyAssigned(currentlyAssigned);
            }


            sql = "SELECT * FROM Issue WHERE issueID = ?";

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, issueID);       // Changed setString to setInt

            rs = stmt.executeQuery();

            if (rs.next()) {
                issue = new IssueBean();
                issue.setIssueID(rs.getString("issueID"));
                issue.setTitle(rs.getString("title"));
                issue.setCategory(rs.getString("category"));
                issue.setStatus(rs.getString("status"));
                issue.setDescription(rs.getString("description"));
                issue.setResolutionDetails(rs.getString("resolutionDetails"));
                issue.setDateTimeReported(rs.getString("dateTimeReported"));
                issue.setDateTimeResolved(rs.getString("dateTimeResolved"));


                sql = "SELECT * FROM Comment WHERE issueID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, issueID);    // Changed setString to setInt

                rs = stmt.executeQuery();

                while (rs.next()) {
                    CommentBean commentBean = new CommentBean();
                    commentBean.setComment(rs.getString("comment"));
                    commentBean.setDateTimePosted(rs.getString("dateTimePosted"));
                    commentBean.setUsername(rs.getString("username"));
                    issue.addComment(commentBean);
                }

                sql = "SELECT k.keywordID, k.keyword FROM IssueKeyword ik JOIN Keyword k ON ik.keywordID = k.keywordID WHERE ik.issueID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, issueID);   // Changed setString to setInt
                rs = stmt.executeQuery();

                while (rs.next()) {
                    KeywordBean keywordBean = new KeywordBean();
                    keywordBean.setKeywordID(rs.getString("keywordID"));
                    keywordBean.setKeyword(rs.getString("keyword"));
                    issue.addKeyword(keywordBean);
                }


                if(keyword != null && !keyword.isEmpty()) {
                    sql = "INSERT INTO Keyword (keyword) VALUES (?)";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, keyword);
                    stmt.executeUpdate();
                }

            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return SUCCESS;
    }
}
