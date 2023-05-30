package app;

import com.opensymphony.xwork2.ActionSupport;

public class AddCommentAction extends ActionSupport {
    private String issueID;
    private String comment;

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
