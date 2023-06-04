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
    private Map<String, Integer> staffAssignedIssues;
    private double averageTimeToResolve;
    private ArrayList<IssueBean> longestIssues;
    private String formattedAverageTimeToResolve;
    private Map<String, Integer> knowledgeBaseArticleCounts;

    // Initialising maps and arraylist in the constructor
    public StatisticsAction() {
        totalCategoryIssues = new HashMap<>();
        totalStatusIssues = new HashMap<>();
        staffAssignedIssues = new HashMap<>();
        longestIssues = new ArrayList<>();
        knowledgeBaseArticleCounts = new HashMap<>();
    }

    // Getters
    public Map<String, Integer> getTotalCategoryIssues() { return totalCategoryIssues; }
    public Map<String, Integer> getTotalStatusIssues() { return totalStatusIssues; }
    public Map<String, Integer> getStaffAssignedIssues() { return staffAssignedIssues; }
    public double getAverageTimeToResolve() { return averageTimeToResolve; }
    public ArrayList<IssueBean> getLongestIssues() { return longestIssues; }
    public String getFormattedTimeToResolve() { return formattedAverageTimeToResolve; }
    public Map<String, Integer> getKnowledgeBaseArticleCounts() { return knowledgeBaseArticleCounts; }

    // Method to format the average time to resolve
    public void setFormattedAverageTimeToResolve(double duration) {
        int days = (int) duration / (24 * 60);
        int hours = (int) (duration / 60) % 24;
        int minutes = (int) duration % 60;
        formattedAverageTimeToResolve = String.format("%d days, %d hours, %d minutes", days, hours, minutes);
    }

    public String execute() {
        try (Connection connection = DBUtil.getConnection()) {

            // Fetch total issues by category
            String sql = "SELECT category, COUNT(*) as total FROM Issue GROUP BY category";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalCategoryIssues.put(resultSet.getString("category"), resultSet.getInt("total"));
                }
            }

            // Fetch total issues by status
            sql = "SELECT status, COUNT(*) as total FROM Issue GROUP BY status";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalStatusIssues.put(resultSet.getString("status"), resultSet.getInt("total"));
                }
            }

            // Fetch issues assigned to each staff member
            sql = "SELECT ui.username, COUNT(*) as total FROM StaffIssue ui JOIN [User] u ON ui.username = u.username WHERE u.role = 'staff' GROUP BY ui.username";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    staffAssignedIssues.put(resultSet.getString("username"), resultSet.getInt("total"));
                }
            }

            // Fetch average time to resolve issues
            sql = "SELECT AVG(DATEDIFF(MINUTE, dateTimeReported, dateTimeResolved)) as average FROM Issue WHERE dateTimeResolved IS NOT NULL";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    averageTimeToResolve = resultSet.getDouble("average");
                }
            }

            // Fetch top 5 longest unresolved issues
            sql = "SELECT TOP 5 * FROM Issue WHERE dateTimeResolved IS NULL ORDER BY dateTimeReported";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
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
            }

            sql = "SELECT i.title, kba.viewCount as viewCount FROM KnowledgeBaseArticle kba JOIN Issue i ON kba.issueID = i.issueID GROUP BY i.title, kba.viewCount";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    knowledgeBaseArticleCounts.put(resultSet.getString("title"), resultSet.getInt("viewCount"));
                }
            }

            // Format the average time to resolve
            setFormattedAverageTimeToResolve(averageTimeToResolve);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return SUCCESS;
    }
}
