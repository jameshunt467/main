package app;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import java.sql.*;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import java.time.LocalDateTime;

public class ProcessIssueAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private String issueDescription;
    private String issueTitle;
    private String issueCategory;
    private LocalDateTime dateTimeReported;
    private int issueID;

    // Getter and setter methods for each field
    public int getIssueID() { return issueID; }
    public String getIssueDescription() { return issueDescription; }
    public String getIssueTitle() { return issueTitle; }
    public String getIssueCategory() { return issueCategory; }

    public void setIssueID(int issueID) { this.issueID = issueID; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }

    @Override
    public String execute() {
        try (Connection con = DBUtil.getConnection()) {
            // Retrieve the current student from the session
            StudentBean student = (StudentBean) ActionContext.getContext().getSession().get("user");
            String username = student.getUsername();

            // Validate category
            if (!issueCategory.matches("Network|Software|Hardware|Email|Account")) {
                addActionError("Invalid category. Must be one of: Network, Software, Hardware, Email, Account");
                return ERROR;
            }

            // Create a statement for inserting the new issue into the database
            PreparedStatement stmtIssue = con.prepareStatement(
                    "INSERT INTO Issue (title, category, status, description, dateTimeReported) VALUES (?, ?, 'New', ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            stmtIssue.setString(1, issueTitle);
            stmtIssue.setString(2, issueCategory);
            stmtIssue.setString(3, issueDescription);
            stmtIssue.setObject(4, LocalDateTime.now()); // current time as dateTimeReported

            // Execute the query
            stmtIssue.executeUpdate();

            // Retrieve the generated key (the new issue ID)
            try(ResultSet generatedKeys = stmtIssue.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int issueId = generatedKeys.getInt(1);
                    this.issueID = issueId;

                    // Now we insert into UserIssue
                    PreparedStatement stmtUserIssue = con.prepareStatement(
                            "INSERT INTO UserIssue (username, issueID) VALUES (?, ?)"
                    );

                    stmtUserIssue.setString(1, username);
                    stmtUserIssue.setInt(2, issueId);

                    // execute the query
                    stmtUserIssue.executeUpdate();

                    return SUCCESS;
                } else {
                    // Handle case where no generated key was returned
                    addActionError("An error occurred when creating the issue.");
                    return ERROR;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("An error occurred when processing the issue.");
            return ERROR;
        }
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
