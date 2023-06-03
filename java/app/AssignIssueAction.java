package app;

import com.opensymphony.xwork2.ActionSupport;

import app.BaseAction;

import java.sql.*;

public class AssignIssueAction extends BaseAction {
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

            // If there is an existing assignment for this issue, remove it
            sql = "DELETE FROM UserIssue WHERE issueID = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, issueID);
            stmt.executeUpdate();

            // Step 1: assign staff member to issue
            sql = "INSERT INTO UserIssue (issueID, username) VALUES (?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, issueID);
            stmt.setString(2, staffUsername);
            stmt.executeUpdate();

            // Step 2: Notify the staff member involved in the issue
            sql = "SELECT * FROM Issue WHERE issueID = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, issueID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String issueTitle = resultSet.getString("title");

                    // Step 3: Create the notification
                    String message = getLoggedInUser().getUsername() + " has assigned you to: '" + issueTitle + "'";
                    sql = "INSERT INTO Notification (issueID, username, hasSeen, message, dateTimeSent) VALUES (?, ?, 0, ?, CURRENT_TIMESTAMP)";
                    stmt = con.prepareStatement(sql);
                    stmt.setInt(1, issueID);
                    stmt.setString(2, staffUsername);
                    stmt.setString(3, message);
                    stmt.executeUpdate();
                }
            }
        }

        return SUCCESS;
    }
}
