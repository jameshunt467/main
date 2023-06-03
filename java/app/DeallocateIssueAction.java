package app;

import java.sql.*;
import javax.sql.*;

import app.BaseAction;

public class DeallocateIssueAction extends BaseAction {
    private int issueID;
    private String username;

    public int getIssueID() { return this.issueID; }
    public String getUsername() { return this.username; }

    public void setIssueID(int issueID) { this.issueID = issueID; }
    public void setUsername(String username) { this.username = username; }

    public String execute() {
        try (Connection connection = DBUtil.getConnection()) {
            String sql = "DELETE FROM UserIssue WHERE username = ? AND issueID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setInt(2, issueID);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0) {
                return "success";
            } else {
                // no rows were affected, handle it accordingly
                return "error";
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
