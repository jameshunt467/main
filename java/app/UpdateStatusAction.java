package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateStatusAction extends ActionSupport {
    private int issueID;
    private String newStatus;

    public int getIssueID() { return issueID; }
    public void setIssueID(int issueID) { this.issueID = issueID; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String execute() throws Exception {
        try (Connection connection = DBUtil.getConnection()) {
            String sql = "UPDATE Issue SET status = ? WHERE issueID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newStatus);
            statement.setInt(2, issueID);

            statement.executeUpdate();
        }

        return SUCCESS;
    }
}