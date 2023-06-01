package app;

import com.opensymphony.xwork2.ActionSupport;

import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;
import org.apache.struts2.interceptor.SessionAware; // retrieve session
import java.util.Map;                               // retrieve session
import java.time.LocalDateTime;                     // db input

// Conains the logic for processing form submission

public class ProcessIssueAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private String issueDescription;    // Field in 'submit-issue' jsp
    private String issueTitle;
    private String issueCategory;
    private LocalDateTime dateTimeReported;

    // getter and setter for issueDescription
    public String getIssueDescription() { return issueDescription; }
    public String getIssueTitle() { return issueTitle; }
    public String getIssueCategory() { return issueCategory; }

    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }

    @Override
    public String execute() throws Exception {
        // create connection
        try (Connection con = DBUtil.getConnection()) {
            String username = (String) session.get("username");

            // Validate category
            if (!issueCategory.matches("Network|Software|Hardware|Email|Account")) {
                addActionError("Invalid category. Must be one of: Network, Software, Hardware, Email, Account");
                return ERROR;
            }
            // create a statement
            PreparedStatement stmtIssue = con.prepareStatement(
                "INSERT INTO Issue (title, category, status, description, dateTimeReported) VALUES (?, ?, 'New', ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            stmtIssue.setString(1, issueTitle);
            stmtIssue.setString(2, issueCategory);
            stmtIssue.setString(3, issueDescription);
            stmtIssue.setObject(4, LocalDateTime.now()); // current time as dateTimeReported (closest match to SQL.DATETIME, could change to TIMESTAMP)

            // execute the query
            stmtIssue.executeUpdate();

            // Retrieve the generated key (the new issue ID)
            try(ResultSet generatedKeys = stmtIssue.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int issueId = generatedKeys.getInt(1);
    
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
            return ERROR;
        }
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
