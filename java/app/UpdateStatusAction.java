package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class UpdateStatusAction extends ActionSupport {
    private int issueID;
    private String newStatus;

    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String execute() throws Exception {
        try (Connection connection = DBUtil.getConnection()) {
            // Check if status is 'Not Accepted' and change it to 'In Progress'
            if ("Not Accepted".equals(newStatus)) {
                newStatus = "In Progress";
            }

            if ("Resolved".equals(newStatus)) {
                String sql = "UPDATE Issue SET status = ?, dateTimeResolved = ? WHERE issueID = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newStatus);
                statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                statement.setInt(3, issueID);

                statement.executeUpdate();
            } else {
                String sql = "UPDATE Issue SET status = ? WHERE issueID = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newStatus);
                statement.setInt(2, issueID);

                statement.executeUpdate();
            }
        }

        return SUCCESS;
    }
}

