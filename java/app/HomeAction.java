package app;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeAction extends ActionSupport {

    private List<NotificationBean> notifications = new ArrayList<>();
    private ArrayList<IssueBean> issueList = new ArrayList<>();

    public List<NotificationBean> getNotifications() {
        return notifications;
    }

    public ArrayList<IssueBean> getIssueList() {
        return issueList;
    }

    public String execute() throws Exception {
        UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");

        if(user == null) {
            return ERROR;
        }

        // Gather the notifications
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT * FROM Notification WHERE username = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getUsername());

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NotificationBean notification = new NotificationBean();
                    notification.setNotificationID(rs.getInt("notificationID"));
                    notification.setMessage(rs.getString("message"));
                    notification.setDateTimeSent(rs.getString("dateTimeSent"));
                    notification.setUsername(rs.getString("username"));
                    notification.setIssueID(rs.getInt("issueID"));
                    notification.setHasSeen(rs.getBoolean("hasSeen"));
                    notifications.add(notification);
                }
            }
        }

        // Gather the issues
        try (Connection connection = DBUtil.getConnection()) {
            String sql;

            if (Objects.equals(user.role, "staff")) {
                sql = "SELECT i.* FROM Issue i JOIN StaffIssue ui ON i.issueID = ui.issueID WHERE ui.username = ? AND i.issueID NOT IN (SELECT issueID FROM KnowledgeBaseArticle)";
            } else {
                sql = "SELECT i.* FROM Issue i JOIN UserIssue ui ON i.issueID = ui.issueID WHERE ui.username = ?";
            }

            System.out.println(sql);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                IssueBean issue = new IssueBean();
                issue.setIssueID(resultSet.getString("issueID"));
                issue.setTitle(resultSet.getString("title"));
                issue.setCategory(resultSet.getString("category"));
                issue.setStatus(resultSet.getString("status"));
                issue.setDescription(resultSet.getString("description"));
                issue.setResolutionDetails(resultSet.getString("resolutionDetails"));
                issue.setDateTimeReported(resultSet.getString("dateTimeReported"));
                issue.setDateTimeResolved(resultSet.getString("dateTimeResolved"));
                issueList.add(issue);
            }
        }

        return SUCCESS;
    }
}
