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

    private int issueID;
    private IssueBean issue;
    private String keyword;

    public String getKeyword() { return keyword; }
    public int getIssueID() { return this.issueID; }
    public IssueBean getIssue() { return issue; }
    public List<String> getStaffMembers() { return staffMembers; }

    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setIssueID(int issueID) { this.issueID = issueID; }
    public void setIssue(IssueBean issue) { this.issue = issue; }    

    public String execute() throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try (Connection connection = DBUtil.getConnection()) {
            
            if(getLoggedInUser().getRole().equals("manager")) {
                // fetch the list of staff members from your database
                String sql = "SELECT username FROM User WHERE role = 'staff'"; // replace the condition with the correct one if needed
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    staffMembers.add(rs.getString("username"));
                }
                rs.close();
                stmt.close();
            }

            String sql = "SELECT * FROM Issue WHERE issueID = ?";

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
                    issue.addComment(rs.getString("comment"));
                }

                sql = "SELECT k.keyword FROM IssueKeyword ik JOIN Keyword k ON ik.keywordID = k.keywordID WHERE ik.issueID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, issueID);   // Changed setString to setInt
                rs = stmt.executeQuery();

                while (rs.next()) {
                    issue.addKeyword(rs.getString("keyword"));
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

    // I need to check user.isManager()
    // if so then display an option to assign issue
    // have a drop down of staff members 
    // after they select, just assign, check if not already assigned.  
    // This can be found by looking at UserIssue and if staff.username isn't there
    // with issueID we can assign them 

}
