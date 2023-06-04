package app;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditKeywordAction extends ActionSupport {
    private String keywordID;
    private String keyword;
    private int issueID;

    // Setters
    public void setKeywordID(String keywordID) { this.keywordID = keywordID; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setIssueID(int issueID) { this.issueID = issueID; }

    // Getter
    public int getIssueID() { return this.issueID; }

    public String execute() {
        // Only perform the update if the keywordID and keyword are not null or empty
        if (keywordID != null && keyword != null && !keyword.isEmpty()) {
            // Check the keyword for invalid characters
            if (!keyword.matches("[a-zA-Z0-9]+")) {
                return ERROR;
            }
            try (Connection connection = DBUtil.getConnection()) {
                // SQL query to check if the new keyword already exists
                String checkSql = "SELECT COUNT(*) FROM Keyword WHERE keyword = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                checkStmt.setString(1, keyword);
                ResultSet rs = checkStmt.executeQuery();
                // If the new keyword already exists, return error
                if (rs.next() && rs.getInt(1) > 0) {
                    return ERROR;
                }
                // SQL query to update the keyword for a given keywordID
                String sql = "UPDATE Keyword SET keyword = ? WHERE keywordID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, keyword);
                statement.setString(2, keywordID);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                return ERROR;
            }
        }
        return SUCCESS;
    }

}
