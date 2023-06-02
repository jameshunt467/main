package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsAction extends ActionSupport {

    private Map<String, Integer> totalCategoryIssues;
    private Map<String, Integer> totalStatusIssues;
    private Map<String, Integer> StaffAssignedIssues;
    private double averageTimeToResolve;
    private ArrayList<IssueBean> longestIssues;

    private String formattedAverageTimeToResolve;

    public StatisticsAction() {
        totalCategoryIssues = new HashMap<>();
        totalStatusIssues = new HashMap<>();
        StaffAssignedIssues = new HashMap<>();
        longestIssues = new ArrayList<>();
    }


    public Map<String, Integer> getTotalCategoryIssues() {
        return totalCategoryIssues;
    }

    public Map<String, Integer> getTotalStatusIssues() {
        return totalStatusIssues;
    }

    public Map<String, Integer> getStaffAssignedIssues() {
        return StaffAssignedIssues;
    }

    public double getAverageTimeToResolve() {
        return averageTimeToResolve;
    }

    public ArrayList<IssueBean> getLongestIssues() {
        return longestIssues;
    }

    public void setFormattedAverageTimeToResolve(double duration) {
        int days = (int) duration / (24 * 60);
        int hours = (int) (duration / 60) % 24;
        int minutes = (int) duration % 60;
        formattedAverageTimeToResolve = String.format("%d days, %d hours, %d minutes", days, hours, minutes);
    }

    public String getFormattedTimeToResolve() {
        return formattedAverageTimeToResolve;
    }

    public String execute() {

        try (Connection connection = DBUtil.getConnection()) {


            String sql = "SELECT category, COUNT(*) as total FROM Issue GROUP BY category";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalCategoryIssues.put(resultSet.getString("category"), resultSet.getInt("total"));
            }



            sql = "SELECT status, COUNT(*) as total FROM Issue GROUP BY status";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalStatusIssues.put(resultSet.getString("status"), resultSet.getInt("total"));
            }


            sql = "SELECT ui.username, COUNT(*) as total FROM UserIssue ui JOIN [User] u ON ui.username = u.username WHERE u.role = 'staff' GROUP BY ui.username";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                StaffAssignedIssues.put(resultSet.getString("username"), resultSet.getInt("total"));
            }


            sql = "SELECT AVG(DATEDIFF(MINUTE, dateTimeReported, dateTimeResolved)) as average FROM Issue WHERE dateTimeResolved IS NOT NULL";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                averageTimeToResolve = resultSet.getDouble("average");
            }

            sql = "SELECT TOP 5 * FROM Issue WHERE dateTimeResolved IS NULL ORDER BY dateTimeReported";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
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
                longestIssues.add(issue);
            }

            setFormattedAverageTimeToResolve(averageTimeToResolve);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return SUCCESS;
    }
}
