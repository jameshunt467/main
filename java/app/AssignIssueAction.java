package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.*;

public class AssignIssueAction extends ActionSupport {
    private int issueID;
    private String staffUsername;

    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String execute() throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            // First check if the staff member is already assigned to this issue
            String sql = "SELECT COUNT(*) FROM UserIssue WHERE issueID = ? AND username = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, issueID);
            stmt.setString(2, staffUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // The staff member is already assigned to this issue
                addActionError("The selected staff member is already assigned to this issue.");
                return ERROR;
            }

            // The staff member is not assigned to this issue, so assign them
            sql = "INSERT INTO UserIssue (issueID, username) VALUES (?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, issueID);
            stmt.setString(2, staffUsername);
            stmt.executeUpdate();
        }

        return SUCCESS;
    }
}
