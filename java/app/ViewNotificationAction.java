package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.SQLException;
import javax.sql.*;
import java.sql.*;
import java.util.*;

public class ViewNotificationAction extends BaseAction {
    private List<NotificationBean> notifications = new ArrayList<>();

    public List<NotificationBean> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationBean> newNotifications) {
        notifications = newNotifications;
    }

    public String execute() throws Exception {
        // Get the logged-in student
        UserBean user = getLoggedInUser();  // returns loggedInUser from BaseAction class

        // If no student is logged in, return error
        if(user == null) {
            return ERROR;
        }

        try (Connection con = DBUtil.getConnection()) {
            // Prepare SQL to get notifications for the logged-in user that are not seen yet
            String sql = "SELECT * FROM Notification WHERE username = ? AND hasSeen = 0";
            PreparedStatement stmt = con.prepareStatement(sql);

            // Set the username in the SQL statement
            stmt.setString(1, user.getUsername());

            // Execute the SQL and get the results
            try(ResultSet rs = stmt.executeQuery()) {
                // Iterate over the result set
                while (rs.next()) {
                    // Create a new NotificationBean for each row in the result set
                    NotificationBean notification = new NotificationBean();
                    notification.setNotificationID(rs.getInt("notificationID"));
                    notification.setMessage(rs.getString("message"));
                    notification.setDateTimeSent(rs.getString("dateTimeSent"));
                    notification.setUsername(rs.getString("username"));
                    notification.setIssueID(rs.getInt("issueID"));
                    notification.setHasSeen(rs.getBoolean("hasSeen"));

                    // Add the notification to the list of notifications
                    notifications.add(notification);
                }
            } catch (SQLException e) {
                // Handle the exception
                e.printStackTrace();
                // You can also throw a custom exception or return an error status if desired
                // For example:
                // return ERROR;
            }
        }

        return SUCCESS;
    }
}
