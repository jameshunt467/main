package app;

import com.opensymphony.xwork2.ActionSupport;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteKeywordAction extends ActionSupport {
    private int keywordID; // The ID of the keyword to delete
    private int issueID; // The ID of the issue to delete the keyword from

    public int getKeywordID() { return keywordID; }
    public void setKeywordID(int keywordID) { this.keywordID = keywordID; }

    public String execute() throws Exception {
        PreparedStatement stmt = null;
        try (Connection connection = DBUtil.getConnection()) {
            String sql = "DELETE FROM IssueKeyword WHERE keywordID = ? AND issueID = ?";

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, keywordID);
            stmt.setInt(2, issueID);
            stmt.executeUpdate();

        } finally {
            if (stmt != null) stmt.close();
        }
        return SUCCESS;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public int getIssueID() {
        return issueID;
    }


}
