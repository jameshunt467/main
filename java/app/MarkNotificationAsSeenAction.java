package app;

import com.opensymphony.xwork2.ActionSupport;
import javax.sql.*;
import java.sql.*;

public class MarkNotificationAsSeenAction extends ActionSupport {
    private int notificationID;

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String execute() throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            // Prepare SQL to mark the notification as seen
            String sql = "UPDATE Notification SET hasSeen = 1 WHERE notificationID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            // Set the notificationID in the SQL statement
            stmt.setInt(1, notificationID);

            // Execute the SQL
            int rowsUpdated = stmt.executeUpdate();

            // If no rows were updated, return ERROR
            if(rowsUpdated == 0) {
                addActionError("An error occurred while marking the notification as seen.");
                return ERROR;
            }
        }

        return SUCCESS;
    }
}
