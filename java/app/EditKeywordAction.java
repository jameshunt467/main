package app;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditKeywordAction extends ActionSupport {
    private String keywordID;
    private String keyword;
    private int issueID;

    public void setKeywordID(String keywordID) {
        this.keywordID = keywordID;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public int getIssueID() {
        return this.issueID;
    }

    public String execute() throws Exception {
        if (keywordID != null && keyword != null && !keyword.isEmpty()) {
            try (Connection connection = DBUtil.getConnection()) {
                String sql = "UPDATE Keyword SET keyword = ? WHERE keywordID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, keyword);
                statement.setString(2, keywordID);
                statement.executeUpdate();
            }
        }
        return SUCCESS;
    }
}
