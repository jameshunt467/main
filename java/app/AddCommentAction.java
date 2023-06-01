package app;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddCommentAction extends ActionSupport {
    private String issueID;
    private String comment;
    private String username; // assuming you will get the username from the session or elsewhere

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssueID() {
        return issueID;
    }


    public String execute() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\MSSQLEXPRESS;databaseName=seng2050_test", "user1", "comp1140isBAE")) {
            UserBean user = (UserBean) ActionContext.getContext().getSession().get("user");
            this.username = user.getUsername();

            System.out.println(user);

            String sql = "INSERT INTO Comment (comment, dateTimePosted, username, issueID) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, comment);
            statement.setString(2, username);
            statement.setString(3, issueID);
            statement.executeUpdate();
        }
        return SUCCESS;
    }
}
